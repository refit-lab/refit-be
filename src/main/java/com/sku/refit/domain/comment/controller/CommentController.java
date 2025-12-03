/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sku.refit.domain.comment.dto.request.CommentRequest;
import com.sku.refit.domain.comment.dto.response.CommentDetailResponse;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "커뮤니티 댓글", description = "커뮤니티 댓글 관련 API")
@RequestMapping("/api/comments")
public interface CommentController {

  @PostMapping("/new")
  @Operation(summary = "새 댓글 작성", description = "특정 게시글의 댓글을 작성합니다.")
  ResponseEntity<BaseResponse<CommentDetailResponse>> createComment(
      @RequestBody @Valid CommentRequest request,
      @Parameter(description = "댓글을 작성할 게시글 식별자", example = "1") @RequestParam Long postId);

  @GetMapping
  @Operation(summary = "특정 게시글의 댓글 조회", description = "특정 게시글의 댓글 리스트를 조회합니다.")
  ResponseEntity<BaseResponse<List<CommentDetailResponse>>> getAllCommentsByPostId(
      @Parameter(description = "댓글을 작성할 게시글 식별자", example = "1") @RequestParam Long postId);

  @PutMapping("{id}")
  @Operation(summary = "특정 댓글 수정", description = "특정 댓글의 내용을 수정합니다.")
  ResponseEntity<BaseResponse<CommentDetailResponse>> updateComment(
      @Parameter(description = "댓글 식별자", example = "1") @PathVariable Long id,
      @RequestBody @Valid CommentRequest request);

  @DeleteMapping("{id}")
  @Operation(summary = "특정 댓글 삭제", description = "특정 댓글을 삭제합니다.(Hard Delete)")
  ResponseEntity<BaseResponse<Void>> deleteComment(
      @Parameter(description = "댓글 식별자", example = "1") @PathVariable Long id);
}
