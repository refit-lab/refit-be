/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sku.refit.domain.comment.dto.request.CommentRequest;
import com.sku.refit.domain.comment.dto.response.CommentDetailResponse;
import com.sku.refit.domain.comment.entity.Comment;
import com.sku.refit.domain.post.entity.Post;
import com.sku.refit.domain.user.entity.User;

@Component
public class CommentMapper {

  public Comment toComment(CommentRequest request, Comment parent, User user, Post post) {
    return Comment.builder()
        .content(request.getContent())
        .parent(parent)
        .user(user)
        .post(post)
        .build();
  }

  public CommentDetailResponse toDetailResponse(
      Comment comment,
      User user,
      Long likeCount,
      Boolean isLiked,
      List<CommentDetailResponse> replies) {

    return CommentDetailResponse.builder()
        .commentId(comment.getId())
        .content(comment.getContent())
        .nickname(comment.getUser().getNickname())
        .profileImageUrl(comment.getUser().getProfileImageUrl())
        .isWriter(comment.getUser().getId().equals(user.getId()))
        .createdAt(comment.getCreatedAt())
        .parentCommentId(comment.getParent() != null ? comment.getParent().getId() : null)
        .likeCount(likeCount)
        .isLiked(isLiked)
        .replies(replies)
        .build();
  }
}
