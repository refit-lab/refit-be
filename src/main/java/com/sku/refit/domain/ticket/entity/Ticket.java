/*
 * Copyright (c) SKU 다시입을Lab
 */
package com.sku.refit.domain.ticket.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.sku.refit.global.common.BaseTimeEntity;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
    name = "ticket",
    indexes = {
        @Index(name = "idx_ticket_user", columnList = "user_id"),
        @Index(name = "idx_ticket_type_target", columnList = "type,target_id"),
        @Index(name = "idx_ticket_token", columnList = "token"),
        @Index(name = "idx_ticket_used_at", columnList = "used_at"),
        @Index(name = "idx_ticket_expires_at", columnList = "expires_at")
    })
public class Ticket extends BaseTimeEntity {

  /** PK (외부 노출 금지) */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 티켓 종류 (EVENT / CLOTH) */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TicketType type;

  /** 티켓 대상 ID - EVENT → eventId - CLOTH → clothId */
  @Column(name = "target_id", nullable = false)
  private Long targetId;

  /** 티켓 소유 사용자 */
  @Column(name = "user_id", nullable = false)
  private Long userId;

  /** QR에 들어가는 검증용 토큰 - 외부 노출 OK - 의미 없는 랜덤 값 - unique 필수 */
  @Column(nullable = false, unique = true, length = 128)
  private String token;

  /** 사용 시각 (null = 미사용) */
  @Column(name = "used_at")
  private LocalDateTime usedAt;

  /** 만료 시각 (null = 만료 X) */
  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  /* =========================
   * Domain Logic
   * ========================= */

  /** 사용 여부 */
  public boolean isUsed() {
    return usedAt != null;
  }

  /** 만료 여부 (미사용 + expiresAt 존재 + 현재 이후) */
  public boolean isExpired(LocalDateTime now) {
    return usedAt == null && expiresAt != null && now.isAfter(expiresAt);
  }

  /** 티켓 사용 처리 (멱등) */
  public void consume(LocalDateTime now) {
    if (this.usedAt != null) {
      return;
    }
    // 만료된 티켓은 사용 처리 불가
    if (isExpired(now)) {
      throw new IllegalStateException("만료된 티켓은 사용할 수 없습니다.");
    }
    this.usedAt = now;
  }

  /** 기본 도메인 유효성 */
  public void validate() {
    if (type == null) throw new IllegalStateException("Ticket type은 필수입니다.");
    if (targetId == null) throw new IllegalStateException("Ticket targetId는 필수입니다.");
    if (userId == null) throw new IllegalStateException("Ticket userId는 필수입니다.");
    if (token == null || token.isBlank()) throw new IllegalStateException("Ticket token은 필수입니다.");
  }

  /** 만료시간 설정(발급 시) */
  public void updateExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }
}