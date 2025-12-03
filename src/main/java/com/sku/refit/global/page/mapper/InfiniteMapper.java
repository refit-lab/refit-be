/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.page.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sku.refit.global.page.response.InfiniteResponse;

@Component
public class InfiniteMapper {

  public <T> InfiniteResponse<T> toInfiniteResponse(
      List<T> content, Long lastCursor, boolean hasNext, int size) {
    return InfiniteResponse.<T>builder()
        .content(content)
        .lastCursor(lastCursor)
        .hasNext(hasNext)
        .size(size)
        .build();
  }
}
