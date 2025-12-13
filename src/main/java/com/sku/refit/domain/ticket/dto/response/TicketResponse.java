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
  }

  /** 사용자 조회용: - EVENT: "사용한 행사"만 조회(usedAt != null) - CLOTH: "받은 티켓" 전체 조회(usedAt 상관 없음) */
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
  }
}
