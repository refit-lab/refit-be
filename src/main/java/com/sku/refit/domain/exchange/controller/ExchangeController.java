/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.controller;

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

import com.sku.refit.domain.exchange.dto.request.ExchangePostRequest;
import com.sku.refit.domain.exchange.dto.response.ExchangePostCardResponse;
import com.sku.refit.domain.exchange.dto.response.ExchangePostDetailResponse;
import com.sku.refit.global.page.response.PageResponse;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "교환 게시글", description = "교환 게시글 관련 API")
@RequestMapping("/api/exchanges")
public interface ExchangeController {

  @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "새 게시글 작성", description = "새 게시글을 작성합니다.")
  ResponseEntity<BaseResponse<ExchangePostDetailResponse>> createExchangePost(
      @Parameter(
              description = "게시글 이미지 리스트",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart(value = "imageList")
          List<MultipartFile> imageList,
      @Parameter(
              description = "게시글 내용",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart(value = "request")
          @Valid
          ExchangePostRequest request);

  @GetMapping
  @Operation(summary = "교환 게시글 목록(페이지) 조회 (위치 기반)")
  ResponseEntity<BaseResponse<PageResponse<ExchangePostCardResponse>>> getExchangePostsByLocation(
      @Parameter(description = "페이지 번호", example = "1") @RequestParam Integer pageNum,
      @Parameter(description = "페이지 크기", example = "4") @RequestParam Integer pageSize,
      @Parameter(description = "위도", example = "37.544018")
          @RequestParam(defaultValue = "37.544018")
          Double latitude,
      @Parameter(description = "경도", example = "126.951592")
          @RequestParam(defaultValue = "126.951592")
          Double longitude);

  @GetMapping("/{exchangePostId}")
  @Operation(summary = "교환 게시글 단일 조회", description = "교환 게시글 ID로 단일 게시글을 조회합니다.")
  ResponseEntity<BaseResponse<ExchangePostDetailResponse>> getExchangePost(
      @Parameter(description = "교환 게시글 ID", example = "1")
      @PathVariable Long exchangePostId
  );

  @PutMapping(value = "/{exchangePostId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "교환 게시글 수정", description = "특정 교환 게시글을 수정합니다.")
  ResponseEntity<BaseResponse<ExchangePostDetailResponse>> updateExchangePost(
      @Parameter(description = "교환 게시글 ID", example = "1") @PathVariable Long exchangePostId,
      @Parameter(
              description = "수정할 게시글 이미지 리스트",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart(value = "imageList", required = false)
          List<MultipartFile> imageList,
      @Parameter(
              description = "수정할 게시글 내용",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart(value = "request")
          @Valid
          ExchangePostRequest request);

  @DeleteMapping("/{exchangePostId}")
  @Operation(summary = "교환 게시글 삭제", description = "특정 교환 게시글을 삭제합니다.")
  ResponseEntity<BaseResponse<Void>> deleteExchangePost(
      @Parameter(description = "교환 게시글 ID", example = "1") @PathVariable Long exchangePostId);
}
