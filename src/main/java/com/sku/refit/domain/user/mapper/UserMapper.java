/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.user.mapper;

import org.springframework.stereotype.Component;

import com.sku.refit.domain.user.dto.response.UserDetailResponse;
import com.sku.refit.domain.user.entity.User;

@Component
public class UserMapper {

  public UserDetailResponse toUserDetailResponse(User user) {
    return UserDetailResponse.builder()
        .userId(user.getId())
        .profileImageUrl(user.getProfileImageUrl())
        .nickname(user.getNickname())
        .username(user.getUsername())
        .locationConsent(user.getLocationConsent())
        .build();
  }
}
