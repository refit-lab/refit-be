/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public enum TicketType {
  @Schema(description = "행사 체크인")
  EVENT,

  @Schema(description = "의류 관련 티켓")
  CLOTH
}
