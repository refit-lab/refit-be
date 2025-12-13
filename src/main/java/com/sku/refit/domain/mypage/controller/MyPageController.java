/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.global.page.response.InfiniteResponse;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "마이페이지", description = "마이페이지 관련 API")
@RequestMapping("/api/my")
public interface MyPageController {

  @GetMapping("/tickets")
  @Operation(
      summary = "내 티켓 리스트 조회",
      description =
          """
          현재 로그인한 사용자의 티켓 목록을 페이징하여 조회합니다.

          ■ 반환 데이터
          - 티켓 ID
          - 티켓 타입 (EVENT / CLOTH)
          - 티켓 상태 (UNUSED / USED / EXPIRED)
          - 티켓명
          - 위치 정보
          - 설명
          - QR payload URL
          - 발급 시각
          - 사용 시각 (사용 완료된 경우)
          - 만료일

          ■ 정렬 기준
          - 발급 시각(createdAt) 기준 내림차순 (최신 발급 티켓 우선)

          ■ 페이징
          - page: 조회할 페이지 번호 (0부터 시작)
          - size: 한 페이지에 포함될 티켓 개수
          """)
  ResponseEntity<BaseResponse<MyTicketsResponse>> getMyTickets(
      @RequestParam int page, @RequestParam int size);

  @GetMapping("/events/joined")
  @Operation(
      summary = "참여한 행사 조회",
      description = "행사 예약시 발급되는 티켓 중 사용 완료된 티켓을 기준으로 최신순으로 참여한 행사 목록을 반환합니다.")
  ResponseEntity<BaseResponse<JoinedEventsResponse>> getJoinedEvents();

  @GetMapping("/posts")
  @Operation(
      summary = "내가 작성한 글 조회",
      description =
          """
          현재 로그인한 사용자가 작성한 게시글 목록을 커서 기반 무한스크롤로 조회합니다.

          ■ 커서 페이징 방식
          - 첫 조회: lastPostId 생략
          - 다음 조회: 직전 응답의 lastCursor 값을 lastPostId로 전달
          - 정렬: id DESC (최신글 먼저)
          - hasNext: 다음 페이지 존재 여부
          - lastCursor: 다음 요청에 사용할 커서(마지막 항목의 postId)
          """)
  ResponseEntity<BaseResponse<InfiniteResponse<PostDetailResponse>>> getMyPosts(
      @Parameter(description = "마지막으로 조회한 게시글 식별자(첫 조회 시 생략)", example = "10")
          @RequestParam(required = false)
          Long lastPostId,
      @Parameter(description = "한 번에 조회할 게시글 개수", example = "10") @RequestParam(defaultValue = "10")
          Integer size);
}
