/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "교환 카테고리 Enum")
public enum PostCategory {
  @Schema(description = "자유질문")
  FREE("자유질문"),
  @Schema(description = "수선꿀팁")
  REPAIR("수선꿀팁"),
  @Schema(description = "정보공유")
  INFO("정보공유");

  private final String ko;
}
