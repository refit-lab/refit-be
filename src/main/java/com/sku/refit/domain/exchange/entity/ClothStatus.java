/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "옷 상태 Enum")
public enum ClothStatus {
  @Schema(description = "상")
  GOOD("상"),
  @Schema(description = "중")
  FAIR("중"),
  @Schema(description = "하")
  BAD("하");

  private final String ko;
}
