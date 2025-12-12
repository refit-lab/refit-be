/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public enum TicketStatus {
  @Schema(description = "발급됨(미사용)")
  ISSUED,

  @Schema(description = "사용됨 (체크인/검증 완료 처리 등)")
  CONSUMED,

  @Schema(description = "만료")
  EXPIRED
}
