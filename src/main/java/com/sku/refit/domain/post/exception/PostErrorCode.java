/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.exception;

import org.springframework.http.HttpStatus;

import com.sku.refit.global.exception.model.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
  POST_NOT_FOUND("POST001", "게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
  INVALID_CATEGORY("POST002", "유효하지 않은 카테고리입니다.", HttpStatus.BAD_REQUEST),
  IMAGE_UPLOAD_FAILED("POST003", "이미지 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_DELETE_FAILED("POST004", "이미지 삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  POST_CREATE_FAILED("POST005", "게시글 생성 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  POST_UPDATE_FAILED("POST006", "게시글 수정 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  POST_DELETE_FAILED("POST007", "게시글 삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
