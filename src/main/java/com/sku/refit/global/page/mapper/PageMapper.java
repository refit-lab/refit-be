/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.page.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.sku.refit.global.page.response.PageResponse;

@Component
public class PageMapper {

  public <T> PageResponse<T> toPageResponse(Page<T> page) {
    return PageResponse.<T>builder()
        .content(page.getContent())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .pageNum(page.getNumber() + 1)
        .pageSize(page.getSize())
        .last(page.isLast())
        .build();
  }
}
