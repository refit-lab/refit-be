/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "옷 사이즈 Enum")
public enum ClothSize {
  @Schema(description = "FREE")
  FREE,
  @Schema(description = "2XS")
  XS2,
  @Schema(description = "XS")
  XS,
  @Schema(description = "S")
  S,
  @Schema(description = "M")
  M,
  @Schema(description = "L")
  L,
  @Schema(description = "XL")
  XL,
  @Schema(description = "XL2")
  XL2,
  @Schema(description = "XL3")
  XL3;
}
