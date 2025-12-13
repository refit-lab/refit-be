/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.exception;

import org.springframework.http.HttpStatus;

import com.sku.refit.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketErrorCode implements BaseErrorCode {
  TICKET_BAD_REQUEST("TICKET001", "요청 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
  TICKET_TOKEN_REQUIRED("TICKET002", "티켓 토큰이 필요합니다.", HttpStatus.BAD_REQUEST),

  TICKET_NOT_FOUND("TICKET0010", "티켓을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  TICKET_ALREADY_USED("TICKET020", "이미 사용된 티켓입니다.", HttpStatus.CONFLICT),

  TICKET_TOKEN_GENERATION_FAILED(
      "TICKET030", "티켓 토큰 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  TICKET_ISSUE_FAILED("TICKET031", "티켓 발급에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  TICKET_VERIFY_FAILED("TICKET032", "티켓 검증에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  TICKET_CONSUME_FAILED("TICKET033", "티켓 사용 처리에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  TICKET_MY_LIST_FAILED("TICKET034", "내 티켓 목록 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
