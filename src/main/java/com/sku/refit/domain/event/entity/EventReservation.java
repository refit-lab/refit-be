/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.entity;

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
    name = "event_reservation",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_event_user",
          columnNames = {"event_id", "user_id"})
    })
public class EventReservation extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  @Builder.Default
  private Integer clothCount = 0;

  @Column(nullable = false)
  private Boolean marketingConsent;
}
