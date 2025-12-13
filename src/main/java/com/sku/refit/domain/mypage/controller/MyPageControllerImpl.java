/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;
import com.sku.refit.domain.mypage.service.MyPageService;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.global.page.response.InfiniteResponse;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyPageControllerImpl implements MyPageController {

  private final MyPageService myPageService;

  @Override
  public ResponseEntity<BaseResponse<MyTicketsResponse>> getMyTickets(
      @RequestParam int page, @RequestParam int size) {
    return ResponseEntity.ok(BaseResponse.success(myPageService.getMyTickets(page, size)));
  }

  @Override
  public ResponseEntity<BaseResponse<JoinedEventsResponse>> getJoinedEvents() {
    return ResponseEntity.ok(BaseResponse.success(myPageService.getJoinedEvents()));
  }

  @Override
  public ResponseEntity<BaseResponse<InfiniteResponse<PostDetailResponse>>> getMyPosts(
      Long lastPostId, Integer size) {

    return ResponseEntity.ok(BaseResponse.success(myPageService.getMyPosts(lastPostId, size)));
  }
}
