/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "UserDetailResponse DTO", description = "사용자 정보 응답 반환")
public class UserDetailResponse {

  @Schema(description = "사용자 식별자", example = "1")
  private Long userId;

  @Schema(
      description = "프로필 이미지 URL",
      example = "http://k.kakaocdn.net/dn/oOPCG/btsPjlOHjk6/6jx0PyBKkHHyCfbV8IY741/img_640x640.jpg")
  private String profileImageUrl;

  @Schema(description = "닉네임", example = "다시입자")
  private String nickname;

  @Schema(description = "아이디(이메일)", example = "example@example.com")
  private String username;

  @Schema(description = "위치 정보 수집 동의 여부", example = "true")
  private Boolean locationConsent;
}
