/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sku.refit.domain.comment.entity.Comment;
import com.sku.refit.domain.post.dto.request.PostRequest;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.domain.post.entity.Post;
import com.sku.refit.domain.post.entity.PostCategory;
import com.sku.refit.domain.user.entity.User;

@Component
public class PostMapper {

  public Post toPost(
      PostCategory postCategory, PostRequest postRequest, List<String> imageUrlList, User user) {

    return Post.builder()
        .title(postRequest.getTitle())
        .content(postRequest.getContent())
        .postCategory(postCategory)
        .imageUrlList(imageUrlList)
        .user(user)
        .build();
  }

  public PostDetailResponse toDetailResponse(
      Post post, Long likeCount, Boolean isLiked, User user) {

    return PostDetailResponse.builder()
        .postId(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .views(post.getViews())
        .likes(likeCount)
        .comments((long) post.getCommentList().size())
        .createdAt(post.getCreatedAt())
        .nickname(post.getUser().getNickname())
        .isAuthor(user != null && post.getUser().getUsername().equals(user.getUsername()))
        .isLiked(isLiked)
        .category(post.getPostCategory())
        .imageUrlList(post.getImageUrlList())
        .commentIdList(post.getCommentList().stream().map(Comment::getId).toList())
        .build();
  }
}
