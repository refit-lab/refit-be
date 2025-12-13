/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "users")
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "profile_image_url", nullable = false)
  private String profileImageUrl;

  @Column(name = "nickname", nullable = false, unique = true)
  private String nickname;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @JsonIgnore
  @Column(name = "password")
  private String password;

  // 위치 정보 수집 동의 여부
  @Column(name = "location_consent", nullable = false)
  private Boolean locationConsent;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Role role = Role.ROLE_USER;

  @Column(name = "exchange_count", nullable = false)
  @Builder.Default
  private Integer exchangeCount = 0;

  @Column(name = "total_reduced_carbon_g", nullable = false)
  @Builder.Default
  private Long totalReducedCarbonG = 0L;

  public void addExchangeCarbon(long deltaG) {
    if (deltaG <= 0) return;
    this.exchangeCount = (this.exchangeCount == null ? 0 : this.exchangeCount) + 1;
    this.totalReducedCarbonG =
        (this.totalReducedCarbonG == null ? 0L : this.totalReducedCarbonG) + deltaG;
  }

  public static User fromOAuth(String email, String nickname, String profileImageUrl) {
    return User.builder()
        .username(email)
        .profileImageUrl(profileImageUrl)
        .nickname(nickname)
        .locationConsent(false)
        .role(Role.ROLE_USER)
        .build();
  }

  public void updateNickname(String newNickname) {
    this.nickname = newNickname;
  }

  public void updateProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void toggleLocationConsent() {
    this.locationConsent = this.locationConsent == null || !this.locationConsent;
  }
}
