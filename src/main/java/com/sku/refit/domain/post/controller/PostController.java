/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.post.dto.request.PostRequest;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.global.page.response.InfiniteResponse;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "커뮤니티 게시글", description = "커뮤니티 게시글 관련 API")
@RequestMapping("/api/posts")
public interface PostController {

  @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "새 게시글 작성", description = "새 게시글을 작성합니다.")
  ResponseEntity<BaseResponse<PostDetailResponse>> createPost(
      @Parameter(
              description = "게시글 내용",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart(value = "request")
          @Valid
          PostRequest request,
      @Parameter(
              description = "게시글 이미지 리스트",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart(value = "imageList", required = false)
          List<MultipartFile> imageList);

  @PostMapping("/{postId}/like")
  @Operation(summary = "게시글 좋아요 토글", description = "게시글 좋아요를 등록/취소합니다.")
  ResponseEntity<BaseResponse<Boolean>> togglePostLike(
      @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId);

  @GetMapping("/admin")
  @Operation(summary = "[관리자] 게시글 전체 조회", description = "전체 게시글 리스트를 조회합니다.")
  ResponseEntity<BaseResponse<List<PostDetailResponse>>> getAllPosts();

  @GetMapping
  @Operation(summary = "카테고리별 게시글 전체 조회", description = "특정 카테고리의 게시글 리스트를 조회합니다.")
  ResponseEntity<BaseResponse<InfiniteResponse<PostDetailResponse>>> getPostByCategory(
      @Parameter(
              description = "게시글 카테고리",
              schema =
                  @Schema(
                      type = "string",
                      allowableValues = {"FREE", "REPAIR", "INFO"},
                      example = "FREE"))
          @RequestParam
          String category,
      @Parameter(description = "마지막으로 조회한 게시글 식별자(첫 조회 시 생략)", example = "3")
          @RequestParam(required = false)
          Long lastPostId,
      @Parameter(description = "한 번에 조회할 게시글 개수", example = "3") @RequestParam(defaultValue = "3")
          Integer size);

  @GetMapping("/{id}")
  @Operation(summary = "게시글 단일 조회", description = "특정 게시글의 상세 내용을 조회합니다.")
  ResponseEntity<BaseResponse<PostDetailResponse>> getPostById(
      @Parameter(description = "게시글 식별자", example = "1") @PathVariable Long id);

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "게시글 수정", description = "특정 게시글의 상세 내용을 수정합니다.")
  ResponseEntity<BaseResponse<PostDetailResponse>> updatePostById(
      @Parameter(description = "게시글 식별자", example = "1") @PathVariable Long id,
      @Parameter(
              description = "수정할 이미지 리스트",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart(value = "imageList", required = false)
          List<MultipartFile> imageList,
      @Parameter(
              description = "게시글 수정 내용",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @Valid
          @RequestPart(value = "request")
          PostRequest request);

  @DeleteMapping("/{id}")
  @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
  ResponseEntity<BaseResponse<Void>> deletePost(
      @Parameter(description = "게시글 식별자", example = "1") @PathVariable Long id);
}
