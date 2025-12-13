/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.sku.refit.domain.exchange.entity.ClothSize;
import com.sku.refit.domain.exchange.entity.ClothStatus;
import com.sku.refit.domain.exchange.entity.ExchangeCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "PostDetailResponse DTO", description = "교환글 상세 정보 응답 반환")
public class ExchangePostDetailResponse {

  @Schema(description = "교환 게시글 식별자", example = "1")
  private Long exchangePostId;

  @Schema(description = "게시글 작성자", example = "김다입")
  private String nickname;

  @Schema(description = "이미지 URL 리스트")
  private List<String> imageUrlList;

  @Schema(description = "교환 카테고리", example = "PANTS")
  private ExchangeCategory category;

  @Schema(description = "교환 제목", example = "스판 여성용 빈티지 청바지")
  private String title;

  @Schema(description = "옷 사이즈", example = "M")
  private ClothSize size;

  @Schema(description = "옷 상태", example = "GOOD")
  private ClothStatus status;

  @Schema(description = "선호 카테고리")
  private List<ExchangeCategory> preferCategoryList;

  @Schema(description = "교환 희망 스팟", example = "서울역")
  private String exchangeSpot;

  @Schema(description = "교환 희망 스팟 위도", example = "37.544018")
  private Double spotLatitude;

  @Schema(description = "교환 희망 스팟 경도", example = "126.951592")
  private Double spotLongitude;

  @Schema(description = "작성자 본인 여부", example = "true")
  private Boolean isAuthor;

  @Schema(description = "게시글 작성 시간", example = "2025-12-03T14:37:17")
  private LocalDateTime createdAt;
}
