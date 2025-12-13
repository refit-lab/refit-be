/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.sku.refit.domain.ticket.dto.response.TicketResponse.*;
import com.sku.refit.domain.ticket.entity.Ticket;
import com.sku.refit.domain.ticket.entity.TicketType;
import com.sku.refit.domain.ticket.util.TicketQrPayloadFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketMapper {

  private final TicketQrPayloadFactory qrPayloadFactory;

  /** 발급 요청 + userId + token -> Ticket 엔티티 (하위호환) */
  public Ticket toEntity(TicketType type, Long targetId, Long userId, String token) {
    return toEntity(type, targetId, userId, token, null);
  }

  /** 발급 요청 + userId + token + expiresAt -> Ticket 엔티티 */
  public Ticket toEntity(
      TicketType type, Long targetId, Long userId, String token, LocalDate expiresAt) {

    return Ticket.builder()
        .type(type)
        .targetId(targetId)
        .userId(userId)
        .token(token)
        .expiresAt(expiresAt)
        .build();
  }

  public TicketDetailResponse toDetail(Ticket ticket) {
    return TicketDetailResponse.builder()
        .ticketId(ticket.getId())
        .type(ticket.getType())
        .targetId(ticket.getTargetId())
        .token(ticket.getToken())
        .qrPayload(qrPayloadFactory.create(ticket.getToken()))
        .issuedAt(ticket.getCreatedAt())
        .usedAt(ticket.getUsedAt())
        .expiresAt(ticket.getExpiresAt())
        .build();
  }

  /** 사용자 목록용 응답 */
  public MyTicketItemResponse toMyItem(Ticket ticket) {
    return MyTicketItemResponse.builder()
        .ticketId(ticket.getId())
        .type(ticket.getType())
        .targetId(ticket.getTargetId())
        .token(ticket.getToken())
        .qrPayload(qrPayloadFactory.create(ticket.getToken()))
        .issuedAt(ticket.getCreatedAt())
        .usedAt(ticket.getUsedAt())
        .expiresAt(ticket.getExpiresAt())
        .build();
  }

  /** 검증 응답: 만료된 티켓 */
  public VerifyTicketResponse toVerifyExpired(Ticket ticket) {
    return VerifyTicketResponse.builder()
        .valid(false) // 만료 → 유효하지 않음
        .used(false) // 사용 불가 상태
        .ticketId(ticket.getId())
        .type(ticket.getType())
        .targetId(ticket.getTargetId())
        .qrPayload(qrPayloadFactory.create(ticket.getToken()))
        .issuedAt(ticket.getCreatedAt())
        .usedAt(ticket.getUsedAt())
        .expiresAt(ticket.getExpiresAt())
        .build();
  }

  /** 검증 응답: 존재할 때 */
  public VerifyTicketResponse toVerifyFound(Ticket ticket) {
    return VerifyTicketResponse.builder()
        .valid(true)
        .used(ticket.isUsed())
        .ticketId(ticket.getId())
        .type(ticket.getType())
        .targetId(ticket.getTargetId())
        .qrPayload(qrPayloadFactory.create(ticket.getToken()))
        .issuedAt(ticket.getCreatedAt())
        .usedAt(ticket.getUsedAt())
        .expiresAt(ticket.getExpiresAt())
        .build();
  }

  /** 검증 응답: 없을 때 */
  public VerifyTicketResponse toVerifyNotFound() {
    return VerifyTicketResponse.builder().valid(false).used(false).build();
  }

  /** 사용(체크인) 응답 */
  public ConsumeTicketResponse toConsume(Ticket ticket, boolean consumed) {
    return ConsumeTicketResponse.builder()
        .consumed(consumed)
        .ticketId(ticket.getId())
        .type(ticket.getType())
        .targetId(ticket.getTargetId())
        .qrPayload(qrPayloadFactory.create(ticket.getToken()))
        .usedAt(ticket.getUsedAt())
        .expiresAt(ticket.getExpiresAt())
        .build();
  }
}
