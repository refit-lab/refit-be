/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.service;

import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;

public interface MyPageService {

  /**
   * 사용자의 <b>현재 활성화된 티켓 목록</b>을 페이지 단위로 조회합니다.
   *
   * <p><b>활성 티켓 판단 기준</b>
   *
   * <ul>
   *   <li>아직 사용되지 않은 티켓 (usedAt == null)
   *   <li>만료되지 않은 티켓 (expiresAt == null 또는 expiresAt ≥ 오늘 날짜)
   * </ul>
   *
   * <p>결과는 발급일(createdAt) 기준 최신순으로 정렬됩니다.
   *
   * @param page 조회할 페이지 번호 (0부터 시작)
   * @param size 한 페이지에 포함될 티켓 개수
   * @return 활성 티켓 목록과 페이징 메타 정보를 포함한 응답
   */
  MyTicketsResponse getMyTickets(int page, int size);

  /**
   * 사용자가 <b>실제로 참여(체크인)한 행사 목록</b>을 페이지 단위로 조회합니다.
   *
   * <p>조회 기준은 다음 조건을 만족하는 티켓입니다.
   *
   * <ul>
   *   <li>티켓 타입이 {@code EVENT} 인 경우
   *   <li>이미 사용 처리된 티켓 (usedAt != null)
   * </ul>
   *
   * <p>행사 목록은 사용 시각(usedAt) 기준 최신순으로 반환됩니다.
   *
   * @param page 조회할 페이지 번호 (0부터 시작)
   * @param size 한 페이지에 포함될 행사 개수
   * @return 참여한 행사 목록과 페이징 메타 정보를 포함한 응답
   */
  JoinedEventsResponse getJoinedEvents(int page, int size);

  /**
   * 로그인한 사용자가 작성한 게시글 목록을 페이지 단위로 조회합니다.
   *
   * <p>게시글은 ID 기준 내림차순(최신 작성 글 우선)으로 정렬되며, 각 게시글에는 좋아요 수 및 사용자의 좋아요 여부가 함께 포함됩니다.
   *
   * @param page 조회할 페이지 번호 (0부터 시작)
   * @param size 한 페이지에 포함될 게시글 개수
   * @return 내가 작성한 게시글 목록과 페이징 메타 정보를 포함한 응답
   */
  MyPostsResponse getMyPosts(int page, int size);

  /**
   * 마이페이지 홈 화면에 필요한 정보를 조회합니다.
   *
   * <p><b>로그인 여부에 따른 응답 구성</b>
   *
   * <ul>
   *   <li>비로그인 상태: 로그인 여부(false)만 반환
   *   <li>로그인 상태:
   *       <ul>
   *         <li>사용자 기본 정보
   *         <li>누적 교환 횟수
   *         <li>총 줄인 탄소량(g)
   *         <li>탄소량 변경 이력 목록 (그래프 표시용)
   *       </ul>
   * </ul>
   *
   * @return 마이페이지 홈 응답
   */
  MyHomeResponse getMyHome();

  /**
   * 교환 확정 시 호출되는 메서드입니다.
   *
   * <p>다음 작업을 하나의 트랜잭션으로 처리합니다.
   *
   * <ul>
   *   <li>사용자의 교환 횟수 +1
   *   <li>총 줄인 탄소량 +20g
   *   <li>탄소량 변경 이력(CarbonReductionHistory) 저장
   * </ul>
   */
  void addExchangeCarbon();
}
