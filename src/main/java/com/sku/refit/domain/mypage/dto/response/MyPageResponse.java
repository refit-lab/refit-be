/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sku.refit.domain.mypage.constant.TicketUseStatus;
import com.sku.refit.domain.ticket.entity.TicketType;
import com.sku.refit.domain.user.dto.response.UserDetailResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class MyPageResponse {

  /* =========================
   * Tickets (paged)
   * ========================= */

  @Getter
  @Builder
  public static class MyTicketsResponse {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private List<MyTicketItem> items;
  }

  @Getter
  @Builder
  @Schema(title = "MyTicketItem DTO", description = "마이페이지 티켓 리스트 아이템")
  public static class MyTicketItem {

    @Schema(description = "티켓 ID", example = "10")
    private Long ticketId;

    @Schema(description = "티켓 타입", example = "EVENT")
    private TicketType type;

    @Schema(description = "사용 상태(사용전/사용완료/사용만료)", example = "UNUSED")
    private TicketUseStatus status;

    @Schema(description = "티켓명(표시용)", example = "겨울 의류 나눔 행사")
    private String ticketName;

    @Schema(description = "위치(표시용)", example = "서울 성동구")
    private String location;

    @Schema(description = "설명(표시용)", example = "따뜻한 겨울을 위한 의류 기부 행사입니다.")
    private String description;

    @Schema(
        description = "QR payload(URL)",
        example = "https://api/refitlab.site/ticket?v=1&token=xxx")
    private String url;

    @Schema(description = "발급 시각", example = "2025-12-01T10:00:00")
    private LocalDateTime issuedAt;

    @Schema(description = "사용 시각", example = "2025-12-24T12:30:00")
    private LocalDateTime usedAt;

    @Schema(description = "유효기간", example = "2025-12-24T23:59:59")
    private LocalDate expiresAt;
  }

  /* =========================
   * Joined Events
   * ========================= */

  @Getter
  @Builder
  public static class JoinedEventsResponse {
    private List<JoinedEventItem> items;
  }

  @Getter
  @Builder
  @Schema(title = "JoinedEventItem DTO", description = "참가한 행사 응답")
  public static class JoinedEventItem {

    @Schema(description = "행사 식별자", example = "1")
    private Long eventId;

    @Schema(description = "썸네일 이미지 URL")
    private String thumbnailUrl;

    @Schema(description = "행사명", example = "겨울 의류 나눔 행사")
    private String name;

    @Schema(description = "행사 설명", example = "따뜻한 겨울을 위한 의류 기부 행사입니다.")
    private String description;

    @Schema(description = "행사 날짜", example = "2025-12-24")
    private LocalDate date;

    @Schema(description = "행사 장소", example = "서울 성동구")
    private String location;
  }

  /* =========================
   * Home
   * ========================= */

  @Getter
  @Builder
  @Schema(title = "MyPageHomeResponse DTO", description = "마이페이지 홈(/api/my) 응답")
  public static class MyHomeResponse {

    @Schema(description = "로그인 여부", example = "true")
    private Boolean isLoggedIn;

    @Schema(description = "사용자 정보 (비로그인 시 null)")
    private UserDetailResponse user;

    @Schema(description = "나의 교환 횟수", example = "5")
    private Integer exchangeCount;

    @Schema(description = "총 줄인 탄소량(g)", example = "750")
    private Long totalReducedCarbonG;

    @Schema(description = "탄소량 변경 이력(최신순)")
    private List<CarbonChangeItem> carbonChangeList;
  }

  @Getter
  @Builder
  @Schema(title = "CarbonChangeItem DTO", description = "탄소량 변경 이력 아이템")
  public static class CarbonChangeItem {

    @Schema(description = "변경 일시", example = "2025-12-24T12:30:00")
    private LocalDateTime changedAt;

    @Schema(description = "변경일까지의 누적값", example = "40")
    private Long totalAfterG;

    @Schema(description = "변경량(g). 교환이면 +20", example = "20")
    private Long deltaG;
  }
}
