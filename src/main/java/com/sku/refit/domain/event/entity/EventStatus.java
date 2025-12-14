/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "행사 상태 Enum")
public enum EventStatus {
  @Schema(description = "예정")
  UPCOMING("예정"),

  @Schema(description = "진행중")
  ONGOING("진행중"),

  @Schema(description = "완료")
  ENDED("완료");

  private final String ko;
}
