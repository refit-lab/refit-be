/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.entity;

import jakarta.persistence.*;

import com.sku.refit.domain.user.entity.User;
import com.sku.refit.global.common.BaseTimeEntity;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
    name = "post_like",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "user_id"})})
public class PostLike extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
