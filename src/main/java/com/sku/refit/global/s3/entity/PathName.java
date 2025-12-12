/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.s3.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public enum PathName {
  @Schema(description = "프로필사진")
  PROFILE_IMAGE,
  @Schema(description = "게시글")
  POST,
  @Schema(description = "옷")
  CLOTH,
  @Schema(description = "행사")
  EVENT
}
