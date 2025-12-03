/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.post.dto.request.PostRequest;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.domain.post.entity.Post;
import com.sku.refit.domain.post.exception.PostErrorCode;
import com.sku.refit.domain.post.mapper.PostMapper;
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
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
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

    Post post = postMapper.toPost(request, imageUrlList, user);
    postRepository.save(post);

    return postMapper.toDetailResponse(post, user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PostDetailResponse> getAllPosts() {

    User user = userService.getCurrentUser();
    List<Post> posts = postRepository.findAll();

    return posts.stream().map(post -> postMapper.toDetailResponse(post, user)).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public InfiniteResponse<PostDetailResponse> getPostsByCategory(
      String category, Long lastPostId, Integer size) {

    User user = userService.getCurrentUser();

    Pageable pageable = PageRequest.of(0, size + 1, Sort.by(Sort.Direction.DESC, "id"));
    List<Post> posts;

    if (lastPostId == null) {
      posts = postRepository.findByCategoryListContaining(category, pageable).getContent();
    } else {
      posts =
          postRepository
              .findByCategoryListContainingAndIdLessThan(category, lastPostId, pageable)
              .getContent();
    }

    boolean hasNext = posts.size() > size;
    if (hasNext) {
      posts = posts.subList(0, size);
    }

    List<PostDetailResponse> postResponseList =
        posts.stream().map(post -> postMapper.toDetailResponse(post, user)).toList();

    Long newLastCursor = posts.isEmpty() ? null : posts.getLast().getId();

    return infiniteMapper.toInfiniteResponse(postResponseList, newLastCursor, hasNext, size);
  }

  @Override
  @Transactional(readOnly = true)
  public PostDetailResponse getPostById(Long id) {

    User user = userService.getCurrentUser();
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    return postMapper.toDetailResponse(post, user);
  }

  @Override
  @Transactional
  public PostDetailResponse updatePost(Long id, PostRequest request, List<MultipartFile> images) {

    User user = userService.getCurrentUser();
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    List<String> oldImageUrls = post.getImageUrlList();
    List<String> newImageUrls = new ArrayList<>();

    if (images != null && !images.isEmpty()) {

      try {
        // 기존 이미지 삭제
        for (String imageUrl : oldImageUrls) {
          s3Service.deleteFile(s3Service.extractKeyNameFromUrl(imageUrl));
        }

        // 새 이미지 업로드
        for (MultipartFile image : images) {
          newImageUrls.add(s3Service.uploadImage(PathName.POST, image).getImageUrl());
        }

      } catch (Exception e) {
        throw new CustomException(PostErrorCode.IMAGE_UPLOAD_FAILED);
      }

    } else {
      newImageUrls = oldImageUrls;
    }

    post.update(request.getTitle(), request.getContent(), newImageUrls);

    return postMapper.toDetailResponse(post, user);
  }

  @Override
  @Transactional
  public void deletePost(Long id) {

    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    for (String imageUrl : post.getImageUrlList()) {
      s3Service.deleteFile(s3Service.extractKeyNameFromUrl(imageUrl));
    }

    postRepository.delete(post);
  }
}
