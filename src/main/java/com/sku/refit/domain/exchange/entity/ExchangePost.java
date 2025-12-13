/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
@Table(name = "exchange_post")
public class ExchangePost extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection
  @CollectionTable(
      name = "exchange_post_image_url",
      joinColumns = @JoinColumn(name = "exchange_post_id"))
  @Column(name = "image_url", nullable = false)
  private List<String> imageUrlList = new ArrayList<>();

  @Column(nullable = false)
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ExchangeCategory exchangeCategory;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ClothStatus clothStatus;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ClothSize clothSize;

  @Column(columnDefinition = "TEXT")
  private String description;

  @ElementCollection
  @CollectionTable(
      name = "exchange_post_prefer_category",
      joinColumns = @JoinColumn(name = "exchange_post_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private List<ExchangeCategory> preferCategories = new ArrayList<>();

  @Column(nullable = false)
  private String exchangeSpot;

  @Column(nullable = false)
  private Double spotLatitude;

  @Column(nullable = false)
  private Double spotLongitude;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String letter;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private ExchangeStatus exchangeStatus = ExchangeStatus.BEFORE;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public void update(
      List<String> imageUrlList,
      String title,
      ExchangeCategory category,
      ClothStatus status,
      ClothSize size,
      String description,
      List<ExchangeCategory> preferCategoryList,
      String exchangeSpot,
      Double spotLatitude,
      Double spotLongitude,
      String letter) {
    this.imageUrlList = imageUrlList;
    this.title = title;
    this.exchangeCategory = category;
    this.clothStatus = status;
    this.clothSize = size;
    this.description = description;
    this.preferCategories = preferCategoryList;
    this.exchangeSpot = exchangeSpot;
    this.spotLatitude = spotLatitude;
    this.spotLongitude = spotLongitude;
    this.letter = letter;
  }
}
