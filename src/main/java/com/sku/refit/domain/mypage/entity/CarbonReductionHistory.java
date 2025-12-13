/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.sku.refit.domain.user.entity.User;
import com.sku.refit.global.common.BaseTimeEntity;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
    name = "carbon_reduction_history",
    indexes = {
      @Index(name = "idx_carbon_hist_user", columnList = "user_id"),
      @Index(name = "idx_carbon_hist_changed_at", columnList = "changed_at")
    })
public class CarbonReductionHistory extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "changed_at", nullable = false)
  private LocalDateTime changedAt;

  /** 변경량(g). 교환이면 +20 */
  @Column(name = "delta_g", nullable = false)
  private Long deltaG;
}
