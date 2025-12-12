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

    /**
     * 일반적으로는 "발급 대상"을 명확히 해야 해서 필요합니다. - 사용자 본인에게 발급: userId 생략하고 서버에서 currentUser 사용 - 관리자가 특정
     * 유저에게 발급: userId를 받음
     *
     * <p>지금은 "단순 생성/검증 로직"이 목표이므로 옵션으로 둡니다.
     */
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
