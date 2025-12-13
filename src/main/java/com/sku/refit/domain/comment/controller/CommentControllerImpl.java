/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sku.refit.domain.comment.dto.request.CommentRequest;
import com.sku.refit.domain.comment.dto.response.CommentDetailResponse;
import com.sku.refit.domain.comment.service.CommentService;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentControllerImpl implements CommentController {

  private final CommentService commentService;

  @Override
  public ResponseEntity<BaseResponse<CommentDetailResponse>> createComment(
      @Valid CommentRequest request, @RequestParam Long postId) {

    CommentDetailResponse response = commentService.createComment(request, postId);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> toggleLike(@PathVariable Long id) {

    commentService.toggleLike(id);
    return ResponseEntity.ok(BaseResponse.success());
  }

  @Override
  public ResponseEntity<BaseResponse<List<CommentDetailResponse>>> getAllCommentsByPostId(
      @RequestParam Long postId) {

    List<CommentDetailResponse> response = commentService.getAllCommentsByPostId(postId);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<CommentDetailResponse>> updateComment(
      @PathVariable Long id, @Valid CommentRequest request) {

    CommentDetailResponse response = commentService.updateComment(id, request);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable Long id) {

    commentService.deleteComment(id);
    return ResponseEntity.ok(BaseResponse.success(null));
  }
}
