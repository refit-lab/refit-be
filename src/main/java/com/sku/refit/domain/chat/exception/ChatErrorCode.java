/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.exception;

import org.springframework.http.HttpStatus;

import com.sku.refit.global.exception.model.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements BaseErrorCode {
  CHAT_NOT_FOUND("CHAT001", "채팅이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
  ;

  private final String code;
  private final String message;
  private final HttpStatus status;
}
