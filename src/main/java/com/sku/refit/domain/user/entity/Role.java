/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public enum Role {
  @Schema(description = "사용자")
  ROLE_USER,
  @Schema(description = "관리자")
  ROLE_ADMIN,
  @Schema(description = "개발자")
  ROLE_DEVELOPER;
}
