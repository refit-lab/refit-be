/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.post.dto.request.PostRequest;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.domain.post.service.PostService;
import com.sku.refit.global.page.response.InfiniteResponse;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

  private final PostService postService;

  @Override
  public ResponseEntity<BaseResponse<PostDetailResponse>> createPost(
      @Valid PostRequest request, List<MultipartFile> imageList) {

    PostDetailResponse response = postService.createPost(request, imageList);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<Boolean>> togglePostLike(@PathVariable Long postId) {

    boolean liked = postService.togglePostLike(postId);
    return ResponseEntity.ok(BaseResponse.success(liked));
  }

  @Override
  public ResponseEntity<BaseResponse<List<PostDetailResponse>>> getAllPosts() {

    List<PostDetailResponse> response = postService.getAllPosts();
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<InfiniteResponse<PostDetailResponse>>> getPostByCategory(
      String category, Long lastPostId, Integer size) {

    InfiniteResponse<PostDetailResponse> response =
        postService.getPostsByCategory(category, lastPostId, size);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<PostDetailResponse>> getPostById(Long id) {

    PostDetailResponse response = postService.getPostById(id);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<PostDetailResponse>> updatePostById(
      Long id, List<MultipartFile> imageList, @Valid PostRequest request) {

    PostDetailResponse response = postService.updatePost(id, request, imageList);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deletePost(Long id) {

    postService.deletePost(id);
    return ResponseEntity.ok(BaseResponse.success(null));
  }
}
