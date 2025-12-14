/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;
import com.sku.refit.domain.mypage.service.MyPageService;
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
  public ResponseEntity<BaseResponse<JoinedEventsResponse>> getJoinedEvents(
      @RequestParam int page, @RequestParam int size) {
    return ResponseEntity.ok(BaseResponse.success(myPageService.getJoinedEvents(page, size)));
  }

  @Override
  public ResponseEntity<BaseResponse<MyPostsResponse>> getMyPosts(
      @RequestParam int page, @RequestParam int size) {
    return ResponseEntity.ok(BaseResponse.success(myPageService.getMyPosts(page, size)));
  }

  @Override
  public ResponseEntity<BaseResponse<MyHomeResponse>> getMyHome() {
    return ResponseEntity.ok(BaseResponse.success(myPageService.getMyHome()));
  }
}
