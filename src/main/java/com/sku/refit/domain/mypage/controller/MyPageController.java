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
      description = "사용여부(사용전/사용완료/사용만료), 티켓명, 위치, 유효기간, 설명, url(QR payload)을 반환합니다.")
  ResponseEntity<BaseResponse<MyTicketsResponse>> getMyTickets(
      @RequestParam int page, @RequestParam int size);

  @GetMapping("/events/joined")
  @Operation(
      summary = "참여한 행사 조회",
      description = "Ticket usedAt != null 인 EVENT 티켓을 기준으로 최신순 참여한 행사 목록을 반환합니다.")
  ResponseEntity<BaseResponse<JoinedEventsResponse>> getJoinedEvents();
}
