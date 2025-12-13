/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sku.refit.domain.comment.dto.request.CommentRequest;
import com.sku.refit.domain.comment.dto.response.CommentDetailResponse;
import com.sku.refit.domain.comment.entity.Comment;
import com.sku.refit.domain.comment.entity.CommentLike;
import com.sku.refit.domain.comment.exception.CommentErrorCode;
import com.sku.refit.domain.comment.mapper.CommentMapper;
import com.sku.refit.domain.comment.repository.CommentLikeRepository;
import com.sku.refit.domain.comment.repository.CommentRepository;
import com.sku.refit.domain.post.entity.Post;
import com.sku.refit.domain.post.exception.PostErrorCode;
import com.sku.refit.domain.post.repository.PostRepository;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.domain.user.service.UserService;
import com.sku.refit.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final PostRepository postRepository;
  private final CommentMapper commentMapper;
  private final UserService userService;

  @Override
  @Transactional
  public CommentDetailResponse createComment(CommentRequest request, Long postId) {

    User user = userService.getCurrentUser();

    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

    Comment parent = null;
    if (request.getParentCommentId() != null) {
      parent =
          commentRepository
              .findById(request.getParentCommentId())
              .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
      if (!parent.getPost().getId().equals(postId)) {
        throw new CustomException(CommentErrorCode.COMMENT_NOT_FOUND);
      }
    }

    Comment comment = commentMapper.toComment(request, parent, user, post);

    commentRepository.save(comment);

    log.info(
        "[COMMENT CREATE] commentId={}, postId={}, userId={}, parentId={}",
        comment.getId(),
        postId,
        user.getId(),
        parent != null ? parent.getId() : null);

    return commentMapper.toDetailResponse(comment, user, 0L, false, new ArrayList<>());
  }

  @Override
  @Transactional
  public void toggleLike(Long commentId) {

    User user = userService.getCurrentUser();

    Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

    commentLikeRepository
        .findByCommentAndUser(comment, user)
        .ifPresentOrElse(
            like -> {
              commentLikeRepository.delete(like);
              log.info(
                  "[COMMENT LIKE] UNLIKE - commentId={}, userId={}", comment.getId(), user.getId());
            },
            () -> {
              CommentLike saved =
                  commentLikeRepository.save(
                      CommentLike.builder().comment(comment).user(user).build());

              log.info(
                  "[COMMENT LIKE] LIKE - commentId={}, userId={}, likeId={}",
                  comment.getId(),
                  user.getId(),
                  saved.getId());
            });
  }

  @Override
  public List<CommentDetailResponse> getAllCommentsByPostId(Long postId) {

    User user = userService.getCurrentUser();
    if (!postRepository.existsById(postId)) {
      throw new CustomException(PostErrorCode.POST_NOT_FOUND);
    }

    List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);

    if (comments.isEmpty()) {
      return List.of();
    }

    List<Long> commentIds = comments.stream().map(Comment::getId).toList();

    List<Object[]> likeCounts = commentLikeRepository.countByCommentIds(commentIds);

    Map<Long, Long> likeCountMap = new HashMap<>();
    for (Object[] row : likeCounts) {
      Long commentId = (Long) row[0];
      Long count = (Long) row[1];
      likeCountMap.put(commentId, count);
    }

    Set<Long> likedCommentIds = commentLikeRepository.findLikedCommentIds(commentIds, user.getId());

    Map<Long, CommentDetailResponse> responseMap = new HashMap<>();

    for (Comment comment : comments) {
      responseMap.put(
          comment.getId(),
          commentMapper.toDetailResponse(
              comment,
              user,
              likeCountMap.getOrDefault(comment.getId(), 0L),
              likedCommentIds.contains(comment.getId()),
              new ArrayList<>()));
    }

    List<CommentDetailResponse> result = new ArrayList<>();

    for (Comment comment : comments) {
      CommentDetailResponse response = responseMap.get(comment.getId());

      if (comment.getParent() == null) {
        result.add(response);
      } else {
        CommentDetailResponse parentResponse = responseMap.get(comment.getParent().getId());
        if (parentResponse != null) {
          parentResponse.getReplies().add(response);
        } else {
          result.add(response);
        }
      }
    }

    log.info("[COMMENT LIST] postId={}, commentCount={}", postId, comments.size());

    return result;
  }

  @Override
  @Transactional
  public CommentDetailResponse updateComment(Long id, CommentRequest request) {

    User user = userService.getCurrentUser();
    Comment comment =
        commentRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(user.getId())) {
      throw new CustomException(CommentErrorCode.COMMENT_NOT_FOUND);
    }

    comment.update(request.getContent());

    long likeCount = commentLikeRepository.countByComment(comment);
    boolean isLiked = commentLikeRepository.existsByCommentAndUser(comment, user);

    log.info("[COMMENT UPDATE] commentId={}, userId={}", comment.getId(), user.getId());

    return commentMapper.toDetailResponse(comment, user, likeCount, isLiked, new ArrayList<>());
  }

  @Override
  @Transactional
  public void deleteComment(Long id) {

    User user = userService.getCurrentUser();
    Comment comment =
        commentRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(user.getId())) {
      throw new CustomException(CommentErrorCode.COMMENT_NOT_FOUND);
    }

    Post post = comment.getPost();
    post.getCommentList().remove(comment);

    log.info(
        "[COMMENT DELETE] commentId={}, postId={}, userId={}",
        comment.getId(),
        comment.getPost().getId(),
        user.getId());

    commentRepository.delete(comment);
  }
}
