/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.sku.refit.domain.exchange.entity.ExchangePost;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
    name = "chat_room",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"exchange_post_id", "sender_id", "receiver_id"})
    })
public class ChatRoom extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "exchange_post_id", nullable = false)
  private ExchangePost exchangePost;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private User sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiver;

  @Column(columnDefinition = "TEXT")
  @Builder.Default
  private String lastMessage = null;

  @Column @Builder.Default private LocalDateTime lastMessageAt = LocalDateTime.now();

  @Column @Builder.Default private LocalDateTime senderLastReadAt = LocalDateTime.now();

  @Column @Builder.Default private LocalDateTime receiverLastReadAt = LocalDateTime.now();

  public void markAsRead(Long userId, LocalDateTime time) {
    if (sender.getId().equals(userId)) {
      this.senderLastReadAt = time;
    } else {
      this.receiverLastReadAt = time;
    }
  }
}
