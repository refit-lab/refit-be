/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.post.dto.request.PostRequest;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.domain.post.service.PostService;
import com.sku.refit.global.page.response.InfiniteResponse;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostControllerImpl implements PostController {

  private final PostService postService;

  @Override
  public ResponseEntity<BaseResponse<PostDetailResponse>> createPost(
      @Valid PostRequest request, List<MultipartFile> images) {

    PostDetailResponse response = postService.createPost(request, images);
    return ResponseEntity.ok(BaseResponse.success(response));
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
      Long id, List<MultipartFile> image, @Valid PostRequest request) {

    PostDetailResponse response = postService.updatePost(id, request, image);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deletePost(Long id) {

    postService.deletePost(id);
    return ResponseEntity.ok(BaseResponse.success(null));
  }
}
