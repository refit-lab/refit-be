/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.exception;

import org.springframework.http.HttpStatus;

import com.sku.refit.global.exception.model.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventErrorCode implements BaseErrorCode {
  EVENT_NOT_FOUND("EVENT001", "행사가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

  EVENT_THUMBNAIL_REQUIRED("EVENT010", "행사 대표 사진은 필수입니다.", HttpStatus.BAD_REQUEST),
  EVENT_INVALID_DATE_RANGE("EVENT011", "행사 시작일은 종료일보다 늦을 수 없습니다.", HttpStatus.BAD_REQUEST),
  EVENT_INVALID_CAPACITY("EVENT012", "예약 정원(capacity)은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),

  EVENT_THUMBNAIL_UPLOAD_FAILED(
      "EVENT020", "행사 대표 사진 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  EVENT_THUMBNAIL_DELETE_FAILED(
      "EVENT021", "행사 대표 사진 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  EVENT_CREATE_FAILED("EVENT030", "행사 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  EVENT_UPDATE_FAILED("EVENT031", "행사 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  EVENT_DELETE_FAILED("EVENT032", "행사 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  EVENT_ALREADY_RESERVED("EVENT040", "이미 예약한 행사입니다.", HttpStatus.CONFLICT),
  EVENT_CAPACITY_EXCEEDED("EVENT041", "행사 예약 정원이 초과되었습니다.", HttpStatus.CONFLICT),
  EVENT_RESERVATION_CREATE_FAILED(
      "EVENT042", "행사 예약 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  EVENT_RESERVATION_IMAGE_UPLOAD_FAILED(
      "EVENT050", "예약 이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  EVENT_RESERVATION_IMAGES_DELETE_FAILED(
      "EVENT051", "예약 이미지 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  ;

  private final String code;
  private final String message;
  private final HttpStatus status;
}
