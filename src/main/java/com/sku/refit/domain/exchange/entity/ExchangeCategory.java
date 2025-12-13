/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "교환 카테고리 Enum")
public enum ExchangeCategory {
  @Schema(description = "아우터")
  OUTER("아우터"),
  @Schema(description = "상의")
  SHIRTS("상의"),
  @Schema(description = "하의")
  PANTS("하의"),
  @Schema(description = "신발")
  SHOES("신발"),
  @Schema(description = "액세서리")
  ACCESSORY("액세서리");

  private final String ko;
}
