/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.exception;

import org.springframework.http.HttpStatus;

import com.sku.refit.global.exception.model.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeErrorCode implements BaseErrorCode {
  EXCHANGE_NOT_FOUND("EXCHANGE001", "교환 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
  EXCHANGE_ACCESS_DENIED("EXCHANGE002", "해당 교환 게시글에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
  EXCHANGE_CATEGORY_INVALID("EXCHANGE003", "유효하지 않은 교환 카테고리입니다.", HttpStatus.BAD_REQUEST),
  EXCHANGE_STATUS_INVALID("EXCHANGE004", "유효하지 않은 교환 상태입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
