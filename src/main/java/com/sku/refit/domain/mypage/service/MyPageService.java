/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.service;

import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.global.page.response.InfiniteResponse;

public interface MyPageService {

  /* =========================
   * Tickets
   * ========================= */

  /**
   * 사용자의 <b>현재 활성화된 티켓 목록</b>을 페이징하여 조회합니다.
   *
   * <p>활성 티켓의 기준은 다음과 같습니다.
   *
   * <ul>
   *   <li>아직 사용되지 않은 티켓 (usedAt == null)
   *   <li>만료되지 않은 티켓 (expiresAt == null 또는 expiresAt ≥ 오늘 날짜)
   * </ul>
   *
   * @param page 조회할 페이지 번호 (0부터 시작)
   * @param size 한 페이지에 포함될 티켓 개수
   * @return 활성 티켓 목록과 페이징 정보를 포함한 응답
   */
  MyTicketsResponse getMyTickets(int page, int size);

  /* =========================
   * Joined Events
   * ========================= */

  /**
   * 사용자가 실제로 참여(체크인)한 행사 목록을 조회합니다.
   *
   * <p>이 메서드는 다음 조건을 만족하는 티켓을 기준으로 합니다.
   *
   * <ul>
   *   <li>티켓 타입이 {@code EVENT} 인 경우
   *   <li>티켓이 이미 사용 처리된 경우 (usedAt != null)
   * </ul>
   *
   * @return 사용자가 참여한 행사 목록 응답
   */
  JoinedEventsResponse getJoinedEvents();

  /**
   * 로그인한 사용자가 작성한 게시글 목록을 커서 기반 무한 스크롤 방식으로 조회합니다.
   *
   * @param lastPostId 마지막으로 조회한 게시글 ID (첫 조회 시 {@code null})
   * @param size 한 번에 조회할 게시글 개수
   * @return 내가 작성한 게시글 무한 스크롤 응답
   */
  InfiniteResponse<PostDetailResponse> getMyPosts(Long lastPostId, Integer size);
}
