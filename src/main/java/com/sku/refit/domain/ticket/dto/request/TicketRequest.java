/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.sku.refit.domain.ticket.entity.TicketType;

import lombok.*;

public class TicketRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class IssueTicketRequest {
    @NotNull private TicketType type;
    @NotNull private Long targetId;

    private Long userId;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ConsumeTicketRequest {
    @NotBlank private String token;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class VerifyTicketRequest {
    @NotBlank private String token;
  }
}
