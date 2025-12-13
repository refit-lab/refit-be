/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "마이페이지", description = "마이페이지 관련 API")
@RequestMapping("/api/my")
public interface MyPageController {

  @GetMapping("/tickets")
  @Operation(
      summary = "내 티켓 리스트 조회",
      description =
          """
          현재 로그인한 사용자의 티켓 목록을 페이징하여 조회합니다.

          ■ 반환 데이터
          - 티켓 ID
          - 티켓 타입 (EVENT / CLOTH)
          - 티켓 상태 (UNUSED / USED / EXPIRED)
          - 티켓명
          - 위치 정보
          - 설명
          - QR payload URL
          - 발급 시각
          - 사용 시각 (사용 완료된 경우)
          - 만료일

          ■ 정렬 기준
          - 발급 시각(createdAt) 기준 내림차순 (최신 발급 티켓 우선)

          ■ 페이징
          - page: 조회할 페이지 번호 (0부터 시작)
          - size: 한 페이지에 포함될 티켓 개수
          """)
  ResponseEntity<BaseResponse<MyTicketsResponse>> getMyTickets(
      @RequestParam int page, @RequestParam int size);

  @GetMapping("/events/joined")
  @Operation(
      summary = "참여한 행사 조회",
      description = "행사 예약시 발급되는 티켓 중 사용 완료된 티켓을 기준으로 최신순으로 참여한 행사 목록을 반환합니다.")
  ResponseEntity<BaseResponse<JoinedEventsResponse>> getJoinedEvents();
}
