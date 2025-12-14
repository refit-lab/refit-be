/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

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
@Table(name = "event")
public class Event extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(nullable = false)
  private String location;

  @Column private String detailLink;

  @Column(nullable = false)
  private String thumbnailUrl;

  @Column(nullable = false)
  @Builder.Default
  private Integer totalReservedCount = 0;

  @Column private Integer capacity;

  public void update(
      String name,
      String description,
      LocalDate startDate,
      LocalDate endDate,
      String location,
      String detailLink,
      Integer capacity) {
    this.name = name;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    this.location = location;
    this.detailLink = detailLink;
    this.capacity = capacity;
  }

  public void updateThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public void increaseReservedCount() {
    this.totalReservedCount = (this.totalReservedCount == null ? 0 : this.totalReservedCount) + 1;
  }

  public boolean isFull() {
    if (this.capacity == null) return false;
    int current = (this.totalReservedCount == null) ? 0 : this.totalReservedCount;
    return current >= this.capacity;
  }
}
