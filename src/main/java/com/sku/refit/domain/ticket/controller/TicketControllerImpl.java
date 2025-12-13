/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sku.refit.domain.ticket.dto.request.TicketRequest.*;
import com.sku.refit.domain.ticket.dto.response.TicketResponse.*;
import com.sku.refit.domain.ticket.service.TicketService;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketControllerImpl implements TicketController {

  private final TicketService ticketService;

  @Override
  public ResponseEntity<BaseResponse<TicketDetailResponse>> issueTicket(
      @RequestBody @Valid IssueTicketRequest request) {
    return ResponseEntity.ok(
        BaseResponse.success(
            ticketService.issueTicket(request.getType(), request.getTargetId(), request.getUserId())
        )
    );
  }

  @Override
  public ResponseEntity<BaseResponse<VerifyTicketResponse>> verifyTicket(
      @RequestBody @Valid VerifyTicketRequest request) {
    return ResponseEntity.ok(BaseResponse.success(ticketService.verifyTicket(request)));
  }

  @Override
  public ResponseEntity<BaseResponse<ConsumeTicketResponse>> consumeTicket(
      @RequestBody @Valid ConsumeTicketRequest request) {
    return ResponseEntity.ok(BaseResponse.success(ticketService.consumeTicket(request)));
  }

  @Override
  public ResponseEntity<BaseResponse<List<MyTicketItemResponse>>> getMyUsedEventTickets() {
    return ResponseEntity.ok(BaseResponse.success(ticketService.getMyTicketsUsedEvents()));
  }

  @Override
  public ResponseEntity<BaseResponse<List<MyTicketItemResponse>>> getMyClothTickets() {
    return ResponseEntity.ok(BaseResponse.success(ticketService.getMyClothTickets()));
  }
}
