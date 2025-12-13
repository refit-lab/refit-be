/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.dto.response;

import com.sku.refit.domain.exchange.entity.ExchangeCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "ExchangePostCardResponse DTO", description = "교환글 카드 형식 응답 반환")
public class ExchangePostCardResponse {

  @Schema(description = "썸네일 이미지 URL")
  private String thumbnailImageUrl;

  @Schema(description = "교환 카테고리", example = "PANTS")
  private ExchangeCategory category;

  @Schema(description = "교환 제목", example = "스판 여성용 빈티지 청바지")
  private String title;

  @Schema(description = "교환 희망 스팟", example = "서울역")
  private String exchangeSpot;
}
