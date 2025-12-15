/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.sku.refit.domain.event.entity.EventStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(title = "EventResponse DTO", description = "행사 관련 응답 데이터")
public class EventResponse {

  @Getter
  @Builder
  @Schema(title = "EventDetailResponse DTO", description = "행사 상세 조회 응답")
  public static class EventDetailResponse {

    @Schema(description = "현재 사용자가 해당 행사를 이미 예약했는지 여부 (비로그인 시 null)", example = "true")
    private Boolean isReserved;

    /* ===== 상단 ===== */

    @Schema(description = "행사 식별자", example = "1")
    private Long eventId;

    @Schema(description = "누적 예약 인원", example = "25")
    private Integer totalReservedCount;

    @Schema(description = "행사 대표 이미지 URL")
    private String thumbnailUrl;

    @Schema(description = "행사명", example = "겨울 의류 나눔 행사")
    private String name;

    @Schema(description = "행사 설명", example = "따뜻한 겨울을 위한 의류 기부 행사입니다.")
    private String description;

    @Schema(description = "행사 상세 링크", example = "https://example.com/events/1")
    private String detailLink;

    /* ===== 중간 ===== */

    @Schema(description = "시작 날짜", example = "2025-12-24")
    private LocalDate startDate;

    @Schema(description = "종료 날짜", example = "2025-12-26")
    private LocalDate endDate;

    @Schema(description = "행사 장소", example = "서울 성동구")
    private String location;

    @Schema(description = "예약 정원", example = "100")
    private Integer capacity;

    /* ===== 하단 ===== */

    @Schema(description = "최근 등록된 예약 이미지 4장 URL 리스트")
    private List<String> recentImageUrlList;

    @Schema(description = "최근 4장을 제외한 의류 수량", example = "21")
    private Integer clothCountExceptRecent4;
  }

  @Getter
  @Builder
  @Schema(title = "EventImageResponse DTO", description = "행사 예약 이미지 응답")
  public static class EventImageResponse {

    @Schema(description = "이미지 정렬 순서(ID 기준 최신순)", example = "15")
    private Long order;

    @Schema(description = "이미지 URL")
    private String imageUrl;
  }

  @Getter
  @Builder
  @Schema(title = "EventReservationResponse DTO", description = "행사 예약 결과 응답")
  public static class EventReservationResponse {

    @Schema(description = "행사 식별자", example = "1")
    private Long eventId;

    @Schema(description = "예약 완료 여부", example = "true")
    private Boolean reserved;

    @Schema(description = "예약 완료 후 누적 예약 인원", example = "28")
    private Integer totalReservedCount;
  }

  /* ===== 리스트용 ===== */

  @Getter
  @Builder
  @Schema(title = "EventCardResponse DTO", description = "다가오는 행사 카드형 응답")
  public static class EventCardResponse {

    @Schema(description = "행사 식별자", example = "1")
    private Long eventId;

    @Schema(description = "행사 대표 이미지 URL")
    private String thumbnailUrl;

    @Schema(description = "D-day (행사까지 남은 일 수)", example = "5")
    private Long dday;

    @Schema(description = "행사명", example = "겨울 의류 나눔 행사")
    private String name;

    @Schema(description = "행사 설명", example = "따뜻한 겨울을 위한 의류 기부 행사입니다.")
    private String description;

    @Schema(description = "행사 장소", example = "서울 성동구")
    private String location;
  }

  @Getter
  @Builder
  @Schema(title = "EventSimpleResponse DTO", description = "예정/종료 행사 간단 응답")
  public static class EventSimpleResponse {

    @Schema(description = "행사 식별자", example = "1")
    private Long eventId;

    @Schema(description = "행사 대표 이미지 URL")
    private String thumbnailUrl;

    @Schema(description = "행사명", example = "겨울 의류 나눔 행사")
    private String name;

    @Schema(description = "시작 날짜", example = "2025-12-24")
    private LocalDate startDate;

    @Schema(description = "행사 장소", example = "서울 성동구")
    private String location;

    @Schema(description = "설명", example = "따뜻한 겨울을 위한 의류 기부 행사입니다.")
    private String description;
  }

  @Getter
  @Builder
  @Schema(title = "EventGroupResponse DTO", description = "행사 분류별 리스트 응답")
  public static class EventGroupResponse {

    @Schema(description = "다가오는 행사")
    private EventCardResponse upcoming;

    @Schema(description = "예정된 행사")
    private EventSimpleResponse scheduled;

    @Schema(description = "종료된 행사")
    private EventSimpleResponse ended;
  }

  @Getter
  @Builder
  public static class EventPagedResponse {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private List<EventListItem> items;
  }

  @Getter
  @Builder
  public static class EventListItem {

    @Schema(description = "행사 식별자", example = "1")
    private Long eventId;

    @Schema(description = "행사명", example = "겨울 의류 나눔 행사")
    private String name;

    @Schema(description = "시작 날짜", example = "2025-12-24")
    private LocalDate startDate;

    @Schema(description = "행사 장소", example = "서울 성동구")
    private String location;

    @Schema(description = "누적 예약 인원", example = "25")
    private Integer reservedCount;

    @Schema(description = "예약 정원", example = "100")
    private Integer capacity;

    @Schema(description = "행사 상태", example = "진행중")
    private EventStatus status;
  }
}
