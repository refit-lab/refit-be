/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.exception;

import com.sku.refit.global.exception.model.BaseErrorCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final BaseErrorCode errorCode;

  public CustomException(BaseErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
