/*
 * Copyright (c) SKU 다시입을Lab
 */
package com.sku.refit.domain.ticket.dto.response;

import java.time.LocalDateTime;

import com.sku.refit.domain.ticket.entity.TicketType;

import lombok.*;

public class TicketResponse {

  @Getter
  @Builder
  public static class TicketDetailResponse {
    private Long ticketId;
    private TicketType type;
    private Long targetId;

    private String token;
    private String qrPayload;

    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;
    private LocalDateTime expiresAt;
  }

  @Getter
  @Builder
  public static class VerifyTicketResponse {
    private boolean valid;
    private boolean used;

    private Long ticketId;
    private TicketType type;
    private Long targetId;

    private String qrPayload;

    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;
    private LocalDateTime expiresAt;
  }

  @Getter
  @Builder
  public static class ConsumeTicketResponse {
    private boolean consumed;

    private Long ticketId;
    private TicketType type;
    private Long targetId;

    private String qrPayload;

    private LocalDateTime usedAt;
    private LocalDateTime expiresAt;
  }

  @Getter
  @Builder
  public static class MyTicketItemResponse {
    private Long ticketId;
    private TicketType type;
    private Long targetId;

    private String qrPayload;
    private String token;

    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;
    private LocalDateTime expiresAt;
  }
}