/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.exchange.dto.request.ExchangePostRequest;
import com.sku.refit.domain.exchange.dto.response.ExchangePostCardResponse;
import com.sku.refit.domain.exchange.dto.response.ExchangePostDetailResponse;
import com.sku.refit.domain.exchange.service.ExchangeService;
import com.sku.refit.global.exception.CustomException;
import com.sku.refit.global.page.exception.PageErrorStatus;
import com.sku.refit.global.page.response.PageResponse;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExchangeControllerImpl implements ExchangeController {

  private final ExchangeService exchangeService;

  @Override
  public ResponseEntity<BaseResponse<ExchangePostDetailResponse>> createExchangePost(
      List<MultipartFile> imageList, @Valid ExchangePostRequest request) {

    ExchangePostDetailResponse response = exchangeService.createExchangePost(imageList, request);

    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<PageResponse<ExchangePostCardResponse>>>
      getExchangePostsByLocation(
          Integer pageNum, Integer pageSize, Double latitude, Double longitude) {

    if (pageNum < 1) {
      throw new CustomException(PageErrorStatus.PAGE_NOT_FOUND);
    }
    if (pageSize < 1) {
      throw new CustomException(PageErrorStatus.PAGE_SIZE_ERROR);
    }

    Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
    PageResponse<ExchangePostCardResponse> exchangePostCardResponsePageResponse;

    exchangePostCardResponsePageResponse =
        exchangeService.getExchangePostsByLocation(pageable, latitude, longitude);

    return ResponseEntity.ok(BaseResponse.success(exchangePostCardResponsePageResponse));
  }

  @Override
  public ResponseEntity<BaseResponse<ExchangePostDetailResponse>> getExchangePost(
      Long exchangePostId) {

    ExchangePostDetailResponse response =
        exchangeService.getExchangePost(exchangePostId);

    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<ExchangePostDetailResponse>> updateExchangePost(
      Long exchangePostId, List<MultipartFile> imageList, @Valid ExchangePostRequest request) {

    ExchangePostDetailResponse response =
        exchangeService.updateExchangePost(exchangePostId, imageList, request);

    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deleteExchangePost(Long exchangePostId) {

    exchangeService.deleteExchangePost(exchangePostId);

    return ResponseEntity.ok(BaseResponse.success(null));
  }
}
