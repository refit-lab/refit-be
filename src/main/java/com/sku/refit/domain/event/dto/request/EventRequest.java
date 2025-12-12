/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class EventRequest {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "EventInfoRequest DTO", description = "행사 생성 및 수정 요청 데이터")
  public static class EventInfoRequest {

    @NotBlank(message = "행사명은 필수입니다.")
    @Schema(description = "행사명", example = "2025 21% 파티")
    private String name;

    @NotBlank(message = "행사 설명은 필수입니다.")
    @Schema(description = "행사 설명", example = "파티에 대한 짧은 설명입니다.")
    private String description;

    @NotNull(message = "행사 날짜는 필수입니다.") @Schema(description = "행사 날짜", example = "2025-12-24")
    private LocalDate date;

    @NotBlank(message = "행사 장소는 필수입니다.")
    @Schema(description = "행사 장소", example = "서울 성동구")
    private String location;

    @Schema(description = "행사 상세 링크", example = "https://wearagain.org/48")
    private String detailLink;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(title = "EventRsvRequest DTO", description = "행사 예약 요청 데이터")
  public static class EventRsvRequest {

    @NotBlank(message = "예약자 이름은 필수입니다.")
    @Schema(description = "예약자 이름", example = "김재생")
    private String name;

    @NotBlank(message = "연락처는 필수입니다.")
    @Schema(description = "연락처", example = "010-1234-5678")
    private String phone;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Min(value = 0, message = "옷 수량은 0 이상이어야 합니다.")
    @Schema(description = "가져올 옷 수량", example = "3")
    private Integer clothCount;

    @NotNull(message = "수신 동의 여부는 필수입니다.") @Schema(description = "마케팅/알림 수신 동의 여부", example = "true")
    private Boolean marketingConsent;
  }
}
