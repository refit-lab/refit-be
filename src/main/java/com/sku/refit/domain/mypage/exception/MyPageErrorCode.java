/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.exception;

import org.springframework.http.HttpStatus;

import com.sku.refit.global.exception.model.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MyPageErrorCode implements BaseErrorCode {
  TICKETS_FETCH_FAILED("MYPAGE001", "티켓 목록 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  JOINED_EVENTS_FETCH_FAILED("MYPAGE002", "참여한 행사 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  MY_POSTS_FETCH_FAILED("MYPAGE003", "내가 작성한 글 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  CARBON_ADD_FAILED("MYPAGE004", "탄소량 반영에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  MY_HOME_FETCH_FAILED("MYPAGE005", "내 홈 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  ;
  private final String code;
  private final String message;
  private final HttpStatus status;
}
