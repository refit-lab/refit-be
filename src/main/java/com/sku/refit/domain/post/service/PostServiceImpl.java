/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.post.dto.request.PostRequest;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.domain.post.entity.Post;
import com.sku.refit.domain.post.entity.PostCategory;
import com.sku.refit.domain.post.entity.PostLike;
import com.sku.refit.domain.post.exception.PostErrorCode;
import com.sku.refit.domain.post.mapper.PostMapper;
import com.sku.refit.domain.post.repository.PostLikeRepository;
import com.sku.refit.domain.post.repository.PostRepository;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.domain.user.service.UserService;
import com.sku.refit.global.exception.CustomException;
import com.sku.refit.global.page.mapper.InfiniteMapper;
import com.sku.refit.global.page.response.InfiniteResponse;
import com.sku.refit.global.s3.entity.PathName;
import com.sku.refit.global.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostLikeRepository postLikeRepository;
  private final S3Service s3Service;
  private final UserService userService;
  private final PostMapper postMapper;
  private final InfiniteMapper infiniteMapper;

  @Override
  @Transactional
  public PostDetailResponse createPost(PostRequest request, List<MultipartFile> images) {

    User user = userService.getCurrentUser();
    List<String> imageUrlList = new ArrayList<>();

    if (images != null && !images.isEmpty()) {
      for (MultipartFile image : images) {
        String imageUrl = s3Service.uploadImage(PathName.POST, image).getImageUrl();
        imageUrlList.add(imageUrl);
      }
    }

    PostCategory category;
    try {
      category = PostCategory.valueOf(request.getPostCategory());
    } catch (IllegalArgumentException e) {
      throw new CustomException(PostErrorCode.INVALID_CATEGORY);
    }

    Post post = postMapper.toPost(category, request, imageUrlList, user);
    postRepository.save(post);

    log.info(
        "[POST CREATE] postId={}, userId={}, imageCount={}",
        post.getId(),
        user.getId(),
        imageUrlList.size());

    return postMapper.toDetailResponse(post, 0L, false, user);
  }

  @Override
  @Transactional
  public boolean togglePostLike(Long postId) {

    User user = userService.getCurrentUser();
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    return postLikeRepository
        .findByPostIdAndUserId(postId, user.getId())
        .map(
            like -> {
              postLikeRepository.delete(like);
              log.info("[POST LIKE CANCEL] postId={}, userId={}", postId, user.getId());
              return false;
            })
        .orElseGet(
            () -> {
              PostLike postLike = PostLike.builder().post(post).user(user).build();

              postLikeRepository.save(postLike);
              log.info("[POST LIKE CREATE] postId={}, userId={}", postId, user.getId());
              return true;
            });
  }

  @Override
  @Transactional(readOnly = true)
  public List<PostDetailResponse> getAllPosts() {

    User user = userService.getCurrentUser();
    List<Post> posts = postRepository.findAll();

    if (posts.isEmpty()) {
      return List.of();
    }

    List<Long> postIds = posts.stream().map(Post::getId).toList();

    Map<Long, Long> likeCountMap = new HashMap<>();
    List<Object[]> likeCounts = postLikeRepository.countByPostIds(postIds);
    for (Object[] row : likeCounts) {
      likeCountMap.put((Long) row[0], (Long) row[1]);
    }

    Set<Long> likedPostIds =
        new HashSet<>(postLikeRepository.findLikedPostIds(postIds, user.getId()));

    log.info("[POST LIST] userId={}, postCount={}", user.getId(), posts.size());

    return posts.stream()
        .map(
            post ->
                postMapper.toDetailResponse(
                    post,
                    likeCountMap.getOrDefault(post.getId(), 0L),
                    likedPostIds.contains(post.getId()),
                    user))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public InfiniteResponse<PostDetailResponse> getPostsByCategory(
      String category, Long lastPostId, Integer size) {

    User user = userService.getCurrentUser();

    Pageable pageable = PageRequest.of(0, size + 1, Sort.by(Sort.Direction.DESC, "id"));
    List<Post> posts;

    PostCategory postCategory;
    try {
      postCategory = PostCategory.valueOf(category);
    } catch (IllegalArgumentException e) {
      throw new CustomException(PostErrorCode.INVALID_CATEGORY);
    }

    if (lastPostId == null) {
      posts = postRepository.findByPostCategory(postCategory, pageable).getContent();
    } else {
      posts =
          postRepository
              .findByPostCategoryAndIdLessThan(postCategory, lastPostId, pageable)
              .getContent();
    }

    boolean hasNext = posts.size() > size;
    if (hasNext) {
      posts = posts.subList(0, size);
    }

    List<Long> postIds = posts.stream().map(Post::getId).toList();

    Map<Long, Long> likeCountMap = new HashMap<>();
    if (!postIds.isEmpty()) {
      List<Object[]> likeCounts = postLikeRepository.countByPostIds(postIds);
      for (Object[] row : likeCounts) {
        Long postId = (Long) row[0];
        Long count = (Long) row[1];
        likeCountMap.put(postId, count);
      }
    }

    Set<Long> likedPostIds =
        postIds.isEmpty()
            ? Set.of()
            : new HashSet<>(postLikeRepository.findLikedPostIds(postIds, user.getId()));

    List<PostDetailResponse> postResponseList =
        posts.stream()
            .map(
                post ->
                    postMapper.toDetailResponse(
                        post,
                        likeCountMap.getOrDefault(post.getId(), 0L),
                        likedPostIds.contains(post.getId()),
                        user))
            .toList();

    Long newLastCursor = posts.isEmpty() ? null : posts.getLast().getId();

    log.info(
        "[POST CATEGORY LIST] category={}, lastPostId={}, size={}", category, lastPostId, size);

    return infiniteMapper.toInfiniteResponse(postResponseList, newLastCursor, hasNext, size);
  }

  @Override
  @Transactional
  public PostDetailResponse getPostById(Long id) {

    User user = userService.getCurrentUser();
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    post.increaseViews();

    log.info(
        "[POST DETAIL] postId={}, userId={}, views={}",
        post.getId(),
        user.getId(),
        post.getViews());

    long likeCount = postLikeRepository.countByPostId(post.getId());
    boolean isLiked = postLikeRepository.existsByPostIdAndUserId(post.getId(), user.getId());

    return postMapper.toDetailResponse(post, likeCount, isLiked, user);
  }

  @Override
  @Transactional
  public PostDetailResponse updatePost(Long id, PostRequest request, List<MultipartFile> images) {

    User user = userService.getCurrentUser();
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    if (!post.getUser().getId().equals(user.getId())) {
      throw new CustomException(PostErrorCode.POST_UPDATE_FAILED);
    }

    List<String> oldImageUrls = post.getImageUrlList();
    List<String> newImageUrls = new ArrayList<>();

    if (images != null && !images.isEmpty()) {

      try {
        for (String imageUrl : oldImageUrls) {
          s3Service.deleteFile(s3Service.extractKeyNameFromUrl(imageUrl));
        }

        for (MultipartFile image : images) {
          newImageUrls.add(s3Service.uploadImage(PathName.POST, image).getImageUrl());
        }

        for (String imageUrl : oldImageUrls) {
          s3Service.deleteFile(s3Service.extractKeyNameFromUrl(imageUrl));
        }

      } catch (Exception e) {
        for (String url : newImageUrls) {
          s3Service.deleteFile(s3Service.extractKeyNameFromUrl(url));
        }
        throw new CustomException(PostErrorCode.IMAGE_UPLOAD_FAILED);
      }

    } else {
      newImageUrls = oldImageUrls;
    }

    post.update(request.getTitle(), request.getContent(), newImageUrls);

    long likeCount = postLikeRepository.countByPostId(post.getId());
    boolean isLiked = postLikeRepository.existsByPostIdAndUserId(post.getId(), user.getId());

    log.info("[POST UPDATE COMPLETE] postId={}, userId={}", post.getId(), user.getId());

    return postMapper.toDetailResponse(post, likeCount, isLiked, user);
  }

  @Override
  @Transactional
  public void deletePost(Long id) {

    User user = userService.getCurrentUser();
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    if (!post.getUser().getId().equals(user.getId())) {
      throw new CustomException(PostErrorCode.POST_DELETE_FAILED);
    }

    for (String imageUrl : post.getImageUrlList()) {
      s3Service.deleteFile(s3Service.extractKeyNameFromUrl(imageUrl));
    }

    log.info("[POST DELETE COMPLETE] postId={}, userId={}", post.getId(), user.getId());

    postRepository.delete(post);
  }
}
