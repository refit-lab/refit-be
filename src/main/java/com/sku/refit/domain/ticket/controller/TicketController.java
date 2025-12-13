/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sku.refit.domain.ticket.dto.request.TicketRequest.*;
import com.sku.refit.domain.ticket.dto.response.TicketResponse.*;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "티켓", description = "티켓 관련 API")
@RequestMapping("/api/tickets")
public interface TicketController {

  @PostMapping("/dev/issue")
  @Operation(summary = "[개발자] 티켓 발급", description = "EVENT/CLOTH 티켓을 발급하고 token을 반환합니다.")
  ResponseEntity<BaseResponse<TicketDetailResponse>> issueTicket(
      @RequestBody @Valid IssueTicketRequest request);

  @PostMapping("/admin/verify")
  @Operation(summary = "[관리자] 티켓 검증", description = "token으로 티켓 유효/사용 여부를 확인합니다. (사용 처리 X)")
  ResponseEntity<BaseResponse<VerifyTicketResponse>> verifyTicket(
      @RequestBody @Valid VerifyTicketRequest request);

  @PostMapping("/admin/consume")
  @Operation(summary = "[관리자] 티켓 사용 처리", description = "token으로 티켓을 사용 처리합니다. (멱등 사용 처리)")
  ResponseEntity<BaseResponse<ConsumeTicketResponse>> consumeTicket(
      @RequestBody @Valid ConsumeTicketRequest request);

  @GetMapping("/my/events")
  @Operation(summary = "참가한 행사 조회", description = "사용자가 사용한 EVENT 티켓만 조회합니다.")
  ResponseEntity<BaseResponse<List<MyTicketItemResponse>>> getMyUsedEventTickets();

  @GetMapping("/my/cloth")
  @Operation(summary = "교환 내역 조회", description = "사용자가 받았던 CLOTH 티켓을 모두 조회합니다.")
  ResponseEntity<BaseResponse<List<MyTicketItemResponse>>> getMyClothTickets();
}
