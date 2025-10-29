/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.exception.model;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

  String getCode();

  String getMessage();

  HttpStatus getStatus();
}
