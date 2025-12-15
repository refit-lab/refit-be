/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.exchange.dto.request.ExchangePostRequest;
import com.sku.refit.domain.exchange.dto.response.ExchangePostCardResponse;
import com.sku.refit.domain.exchange.dto.response.ExchangePostDetailResponse;
import com.sku.refit.global.page.response.PageResponse;

public interface ExchangeService {

  /**
   * 교환 게시글 생성
   *
   * @param imageList 첨부 이미지 리스트
   * @param request 교환 게시글 생성 요청 DTO
   * @return 생성된 교환 게시글 상세 응답
   */
  ExchangePostDetailResponse createExchangePost(
      List<MultipartFile> imageList, ExchangePostRequest request);

  /**
   * 교환 게시글 위치 기반 조회 (페이지네이션)
   *
   * @param pageable 페이지 정보
   * @param latitude 사용자 위도
   * @param longitude 사용자 경도
   * @return 교환 게시글 카드 페이지 응답
   */
  PageResponse<ExchangePostCardResponse> getExchangePostsByLocation(
      Pageable pageable, String exchangeCategory, Double latitude, Double longitude);

  ExchangePostDetailResponse getExchangePost(Long exchangePostId);

  /**
   * 교환 게시글 수정
   *
   * @param exchangePostId 교환 게시글 ID
   * @param imageList 수정할 이미지 리스트
   * @param request 교환 게시글 수정 요청 DTO
   * @return 수정된 교환 게시글 상세 응답
   */
  ExchangePostDetailResponse updateExchangePost(
      Long exchangePostId, List<MultipartFile> imageList, ExchangePostRequest request);

  /**
   * 교환 게시글 삭제
   *
   * @param exchangePostId 교환 게시글 ID
   */
  void deleteExchangePost(Long exchangePostId);
}
