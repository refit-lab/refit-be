/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "교환 상태 Enum")
public enum ExchangeStatus {
  @Schema(description = "교환 전")
  BEFORE("교환전"),
  @Schema(description = "교환 중")
  IN_PROGRESS("교환중"),
  @Schema(description = "교환 완료")
  COMPLETED("교환완료");

  private final String ko;
}
