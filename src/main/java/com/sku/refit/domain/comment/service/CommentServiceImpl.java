/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sku.refit.domain.comment.dto.request.CommentRequest;
import com.sku.refit.domain.comment.dto.response.CommentDetailResponse;
import com.sku.refit.domain.comment.entity.Comment;
import com.sku.refit.domain.comment.exception.CommentErrorCode;
import com.sku.refit.domain.comment.mapper.CommentMapper;
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

    Comment comment = commentMapper.toComment(request, user, post);

    commentRepository.save(comment);
    return commentMapper.toDetailResponse(comment, user);
  }

  @Override
  public List<CommentDetailResponse> getAllCommentsByPostId(Long postId) {

    User user = userService.getCurrentUser();
    if (!postRepository.existsById(postId)) {
      throw new CustomException(PostErrorCode.POST_NOT_FOUND);
    }

    List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);

    return comments.stream().map(comment -> commentMapper.toDetailResponse(comment, user)).toList();
  }

  @Override
  @Transactional
  public CommentDetailResponse updateComment(Long id, CommentRequest request) {

    User user = userService.getCurrentUser();
    Comment comment =
        commentRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

    comment.update(request.getContent());

    return commentMapper.toDetailResponse(comment, user);
  }

  @Override
  @Transactional
  public void deleteComment(Long id) {

    Comment comment =
        commentRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

    Post post = comment.getPost();
    post.getCommentList().remove(comment);

    commentRepository.delete(comment);
  }
}
