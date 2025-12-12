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

import com.sku.refit.domain.event.dto.request.EventRequest.*;
import com.sku.refit.domain.event.dto.response.EventResponse.*;
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
  @Operation(summary = "[관리자] 행사 생성", description = "행사를 생성합니다.")
  ResponseEntity<BaseResponse<EventDetailResponse>> createEvent(
      @Parameter(
              description = "행사 정보",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
          @RequestPart("request")
          @Valid
          EventInfoRequest request,
      @Parameter(
              description = "대표 사진(썸네일)",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          @RequestPart("thumbnail")
          MultipartFile thumbnail);

  @PutMapping(value = "/admin/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "[관리자] 행사 수정", description = "행사를 수정합니다.")
  ResponseEntity<BaseResponse<EventDetailResponse>> updateEvent(
      @PathVariable Long id,
      @RequestPart("request") @Valid EventInfoRequest request,
      @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail);

  @DeleteMapping("/admin/{id}")
  @Operation(summary = "[관리자] 행사 삭제", description = "행사 뿐만 아니라 대표사진 및 이미지들까지 모두 삭제합니다.")
  ResponseEntity<BaseResponse<Void>> deleteEvent(@PathVariable Long id);

  /* =========================
   * Public List
   * ========================= */

  @GetMapping("/upcoming")
  @Operation(summary = "예정된 행사 리스트", description = "예정된 행사만 조회합니다. D-day가 가까운 순(오름차순)으로 정렬합니다.")
  ResponseEntity<BaseResponse<List<EventCardResponse>>> getUpcomingEvents();

  @GetMapping("/end")
  @Operation(summary = "종료된 행사 리스트", description = "종료된 행사만 조회합니다. 최근 종료 순(내림차순)으로 정렬합니다.")
  ResponseEntity<BaseResponse<List<EventSimpleResponse>>> getEndedEvents();

  @GetMapping
  @Operation(summary = "행사 3분류 조회", description = "다가오는/예정/종료 3분류로 반환합니다.")
  ResponseEntity<BaseResponse<EventGroupResponse>> getEventGroups();

  /* =========================
   * Detail
   * ========================= */

  @GetMapping("/{id}")
  @Operation(
      summary = "행사 상세 조회",
      description = "행사 예약 여부 + 행사 정보 + 최근 예약 이미지 4장 + 4장 제외 의류수를 반환합니다.")
  ResponseEntity<BaseResponse<EventDetailResponse>> getEventDetail(@PathVariable Long id);

  @GetMapping("/{id}/img")
  @Operation(
      summary = "행사 더보기 이미지 조회",
      description = "해당 행사의 예약에서 업로드된 모든 옷 사진을 최신 등록순으로 반환합니다(페이징 없음).")
  ResponseEntity<BaseResponse<List<EventImageResponse>>> getEventAllReservationImages(
      @PathVariable Long id);

  /* =========================
   * Reservation
   * ========================= */

  @PostMapping(value = "/{id}/rsv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "행사 예약", description = "예약 정보를 저장하고 업로드 이미지는 WebP로 저장합니다.")
  ResponseEntity<BaseResponse<EventReservationResponse>> reserveEvent(
      @PathVariable Long id,
      @RequestPart("request") @Valid EventRsvRequest request,
      @RequestPart(value = "clothImageList", required = false) List<MultipartFile> clothImageList);
}
