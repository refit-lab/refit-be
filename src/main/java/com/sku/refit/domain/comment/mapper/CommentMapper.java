/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.mapper;

import org.springframework.stereotype.Component;

import com.sku.refit.domain.comment.dto.request.CommentRequest;
import com.sku.refit.domain.comment.dto.response.CommentDetailResponse;
import com.sku.refit.domain.comment.entity.Comment;
import com.sku.refit.domain.post.entity.Post;
import com.sku.refit.domain.user.entity.User;

@Component
public class CommentMapper {

  public Comment toComment(CommentRequest request, User user, Post post) {
    return Comment.builder().content(request.getContent()).user(user).post(post).build();
  }

  public CommentDetailResponse toDetailResponse(Comment comment, User user) {

    return CommentDetailResponse.builder()
        .commentId(comment.getId())
        .content(comment.getContent())
        .isWriter(comment.getUser().getId().equals(user.getId()))
        .createdAt(comment.getCreatedAt())
        .build();
  }
}
