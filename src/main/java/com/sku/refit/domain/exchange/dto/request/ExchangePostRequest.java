/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.sku.refit.domain.exchange.entity.ClothSize;
import com.sku.refit.domain.exchange.entity.ClothStatus;
import com.sku.refit.domain.exchange.entity.ExchangeCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "ExchangePostRequest DTO", description = "새 교환 게시물 등록을 위한 데이터 전송")
public class ExchangePostRequest {

  @NotBlank(message = "교환 게시글 제목은 필수입니다.")
  @Schema(description = "교환 게시글 제목", example = "오버핏 흰색 셔츠")
  private String title;

  @NotNull(message = "교환 카테고리는 필수입니다.") @Schema(description = "교환 카테고리", example = "OUTER")
  private ExchangeCategory exchangeCategory;

  @NotNull(message = "옷 상태는 필수입니다.") @Schema(description = "옷 상태", example = "GOOD")
  private ClothStatus clothStatus;

  @NotNull(message = "옷 사이즈는 필수입니다.") @Schema(description = "옷 사이즈", example = "M")
  private ClothSize clothSize;

  @NotBlank(message = "교환 게시글 설명은 필수입니다.")
  @Schema(description = "교환게시글 설명", example = "아이템에 대한 상세 설명을 작성해주세요.")
  private String description;

  @NotEmpty(message = "교환 선호 카테고리는 필수입니다.")
  @Schema(description = "선호 카테고리")
  private List<ExchangeCategory> preferCategoryList;

  @NotBlank(message = "교환 희망 스팟은 필수입니다.")
  @Schema(description = "교환 희망 스팟", example = "서울역")
  private String exchangeSpot;

  @NotNull(message = "교환 희망 스팟 위도는 필수입니다.") @Schema(description = "교환 희망 스팟 위도", example = "37.544018")
  private Double spotLatitude;

  @NotNull(message = "교환 희망 스팟 경도는 필수입니다.") @Schema(description = "교환 희망 스팟 경도", example = "126.951592")
  private Double spotLongitude;

  @NotBlank(message = "교환 편지는 필수입니다.")
  @Schema(description = "교환 편지", example = "이젠 안녕! 청바지야.")
  private String letter;
}
