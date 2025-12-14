/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.event.dto.request.EventRequest.EventInfoRequest;
import com.sku.refit.domain.event.dto.request.EventRequest.EventRsvRequest;
import com.sku.refit.domain.event.dto.response.EventResponse.EventCardResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventDetailResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventGroupResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventImageResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventPagedResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventReservationResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventSimpleResponse;
import com.sku.refit.domain.event.entity.EventStatus;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "행사", description = "행사 관련 API")
@RequestMapping("/api/events")
public interface EventController {

  /* =========================
   * Admin
   * ========================= */

  @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "[관리자] 행사 생성",
      description =
          """
          multipart/form-data 로 요청합니다.

          - request: 행사 정보(JSON)
          - thumbnail: 대표 사진(파일)
          """)
  ResponseEntity<BaseResponse<EventDetailResponse>> createEvent(
      @Parameter(
              description = "행사 정보(JSON 파트)",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart("request")
          @Valid
          EventInfoRequest request,
      @Parameter(
              description = "대표 사진(썸네일 파일 파트)",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart("thumbnail")
          MultipartFile thumbnail);

  @PutMapping(value = "/admin/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "[관리자] 행사 수정",
      description =
          """
          multipart/form-data 로 요청합니다.

          - request: 행사 정보(JSON)
          - thumbnail: 대표 사진(파일, 선택)
          """)
  ResponseEntity<BaseResponse<EventDetailResponse>> updateEvent(
      @Parameter(description = "행사 ID", example = "1") @PathVariable Long id,
      @Parameter(
              description = "행사 정보(JSON 파트)",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart("request")
          @Valid
          EventInfoRequest request,
      @Parameter(
              description = "대표 사진(썸네일 파일 파트, 선택)",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart(value = "thumbnail", required = false)
          MultipartFile thumbnail);

  @DeleteMapping("/admin/{id}")
  @Operation(summary = "[관리자] 행사 삭제", description = "행사 뿐만 아니라 대표사진 및 예약 이미지들까지 모두 삭제합니다.")
  ResponseEntity<BaseResponse<Void>> deleteEvent(
      @Parameter(description = "행사 ID", example = "1") @PathVariable Long id);

  @GetMapping("/admin")
  @Operation(
      summary = "[관리자] 행사 리스트 조회",
      description =
          """
          페이지네이션 기반으로 행사 목록을 조회합니다.

          ■ 조회 옵션
          - status 생략: 전체 조회
          - status=UPCOMING: 예정
          - status=ONGOING: 진행중
          - status=ENDED: 완료
          - q: 검색어(행사명/장소 부분일치)

          ■ 상태 판정 기준 (today 기준)
          - UPCOMING: startDate > today
          - ONGOING: startDate <= today <= endDate
          - ENDED: endDate < today

          ■ 정렬
          - status 미지정(전체): ONGOING → UPCOMING → ENDED (우선순위) + 각 그룹 내 정렬은 아래 기준 적용
          - ONGOING: startDate desc (최근 시작한 진행중부터)
          - UPCOMING: startDate asc (곧 시작하는 행사부터)
          - ENDED: endDate desc (최근 종료된 것부터)
          """)
  ResponseEntity<BaseResponse<EventPagedResponse>> getEvents(
      @Parameter(description = "페이지 번호(0부터 시작)", example = "0") @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "행사 상태 필터(미지정 시 전체). UPCOMING/ONGOING/ENDED", example = "ONGOING")
          @RequestParam(required = false)
          EventStatus status,
      @Parameter(description = "검색어(행사명/장소 부분일치)", example = "서울") @RequestParam(required = false)
          String q);

  /* =========================
   * Public List
   * ========================= */

  @GetMapping("/upcoming")
  @Operation(
      summary = "예정된 행사 리스트",
      description =
          """
          예정된 행사만 조회합니다.

          - UPCOMING: startDate > today
          - 정렬: startDate asc (곧 시작하는 행사부터)
          """)
  ResponseEntity<BaseResponse<List<EventCardResponse>>> getUpcomingEvents();

  @GetMapping("/ended")
  @Operation(
      summary = "종료된 행사 리스트",
      description =
          """
          종료된 행사만 조회합니다.

          - ENDED: endDate < today
          - 정렬: endDate desc (최근 종료된 행사부터)
          """)
  ResponseEntity<BaseResponse<List<EventSimpleResponse>>> getEndedEvents();

  @GetMapping("/group")
  @Operation(
      summary = "행사 그룹 조회 (다가오는/예정/종료 행사 1개씩)",
      description =
          """
          목록(list)이 아니라 그룹 응답입니다.

          - upcoming: 가장 가까운 D-day 1개
          - scheduled: 그 다음 예정 1개
          - ended: 가장 최근 종료 1개
          """)
  ResponseEntity<BaseResponse<EventGroupResponse>> getEventGroups();

  /* =========================
   * Detail
   * ========================= */

  @GetMapping("/{id}")
  @Operation(
      summary = "행사 상세 조회",
      description = "행사 예약 여부 + 행사 정보 + 최근 예약 이미지 4장 + 4장 제외 의류 수를 반환합니다.")
  ResponseEntity<BaseResponse<EventDetailResponse>> getEventDetail(
      @Parameter(description = "행사 ID", example = "1") @PathVariable Long id);

  @GetMapping("/{id}/img")
  @Operation(summary = "행사 더보기 이미지 조회", description = "해당 행사의 예약에서 업로드된 모든 옷 사진을 최신 등록순으로 반환합니다.")
  ResponseEntity<BaseResponse<List<EventImageResponse>>> getEventAllReservationImages(
      @Parameter(description = "행사 ID", example = "1") @PathVariable Long id);

  /* =========================
   * Reservation
   * ========================= */

  @PostMapping(value = "/{id}/rsv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "행사 예약",
      description =
          """
          multipart/form-data 로 요청합니다.

          - request: 예약 정보(JSON)
          - clothImageList: 의류 이미지 리스트(파일, 선택)
          """)
  ResponseEntity<BaseResponse<EventReservationResponse>> reserveEvent(
      @Parameter(description = "행사 ID", example = "1") @PathVariable Long id,
      @Parameter(
              description = "예약 정보(JSON 파트)",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart("request")
          @Valid
          EventRsvRequest request,
      @Parameter(
              description = "의류 이미지 리스트(파일 파트, 선택)",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart(value = "clothImageList", required = false)
          List<MultipartFile> clothImageList);
}
