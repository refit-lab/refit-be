/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.service;

import java.time.LocalDateTime;
import java.util.List;

import com.sku.refit.domain.ticket.dto.request.TicketRequest.*;
import com.sku.refit.domain.ticket.dto.response.TicketResponse.*;
import com.sku.refit.domain.ticket.entity.TicketType;

/**
 * 티켓(Ticket) 관련 주요 기능을 제공하는 서비스 인터페이스입니다.
 *
 * <p>티켓은 QR 코드로 사용자에게 전달되며, QR에는 오직 {@code token} 값만 포함됩니다. 서버는 해당 토큰을 기준으로 티켓의 유효성 및 사용 여부를 검증합니다.
 *
 * <p>주요 기능:
 *
 * <ul>
 *   <li>티켓 발급 (EVENT / CLOTH 공용)
 *   <li>티켓 검증 (사용 처리 없음)
 *   <li>티켓 사용 처리 (체크인, 멱등 보장)
 *   <li>사용자 기준 티켓 조회
 * </ul>
 */
public interface TicketService {

  /* =========================
   * Issue
   * ========================= */

  /**
   * 새로운 티켓을 발급합니다.
   *
   * <p>티켓은 {@code EVENT}, {@code CLOTH} 등 타입에 관계없이 동일한 발급 로직을 사용하며, 발급 시 고유한 {@code token} 이 생성됩니다.
   *
   * <p>생성된 토큰은 QR 코드로 사용자에게 전달되며, 이후 모든 검증/체크인 로직은 해당 토큰을 기준으로 수행됩니다.
   *
   * @param type 티켓 타입
   * @param targetId 대상 ID (EVENT/CLOTH의 식별자)
   * @param userId (옵션) 관리자 발급 시 대상 사용자 ID, null이면 현재 사용자
   * @param expiresAt (옵션) 티켓 만료일
   * @return 발급된 티켓 상세 정보 (type, targetId, token, 발급 시각 등)
   */
  TicketDetailResponse issueTicket(TicketType type, Long targetId, Long userId, LocalDateTime expiresAt);

  /* =========================
   * Verify
   * ========================= */

  /**
   * 티켓을 검증합니다. (사용 처리 없음)
   *
   * <p>주어진 {@code token} 에 해당하는 티켓이 존재하는지, 그리고 이미 사용된 티켓인지 여부를 확인합니다.
   *
   * <p>이 메서드는 <b>티켓을 실제로 사용 처리하지 않으며</b>, QR 스캔 시 사전 검증 용도로 사용됩니다.
   *
   * @param request 티켓 검증 요청 정보 (token)
   * @return 티켓 유효성 및 사용 여부 응답
   */
  VerifyTicketResponse verifyTicket(VerifyTicketRequest request);

  /* =========================
   * Consume (Check-in)
   * ========================= */

  /**
   * 티켓을 사용 처리합니다. (체크인)
   *
   * <p>해당 메서드는 멱등성을 보장하며, 이미 사용된 티켓에 대해 다시 호출되더라도 예외 없이 동일한 상태를 반환합니다.
   *
   * <p>일반적으로 관리자 또는 키오스크에서 QR 스캔 후 실제 입장/수거/처리 시점에 호출됩니다.
   *
   * @param request 티켓 사용 요청 정보 (token)
   * @return 티켓 사용 처리 결과 응답
   */
  ConsumeTicketResponse consumeTicket(ConsumeTicketRequest request);

  /* =========================
   * Query (My Page)
   * ========================= */

  /**
   * 사용자가 <b>사용 완료한 행사(EVENT) 티켓</b>만 조회합니다.
   *
   * <p>행사 티켓의 경우, 실제 체크인이 완료된 행사만 사용자에게 "참여한 행사"로 간주하여 반환합니다.
   *
   * @return 사용자가 사용한 행사 티켓 목록
   */
  List<MyTicketItemResponse> getMyTicketsUsedEvents();

  /**
   * 사용자가 발급받은 <b>CLOTH 티켓 전체</b>를 조회합니다.
   *
   * <p>CLOTH 티켓은 사용 여부와 무관하게, 티켓을 발급받은 시점부터 사용자에게 노출됩니다.
   *
   * @return 사용자가 발급받은 CLOTH 티켓 목록
   */
  List<MyTicketItemResponse> getMyClothTickets();
}
