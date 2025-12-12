/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.event.dto.request.EventRequest.*;
import com.sku.refit.domain.event.dto.response.EventResponse.*;

/**
 * 행사(Event) 관련 주요 기능을 제공하는 서비스 인터페이스입니다.
 *
 * <p>주요 기능:
 *
 * <ul>
 *   <li>행사 생성 / 수정 / 삭제 (관리자)
 *   <li>행사 목록 조회 (예정 / 종료 / 그룹)
 *   <li>행사 상세 조회
 *   <li>행사 예약 및 예약 이미지 업로드
 *   <li>행사 예약 이미지 전체 조회
 * </ul>
 */
public interface EventService {

  /* =========================
   * Admin
   * ========================= */

  /**
   * 새로운 행사를 생성합니다. (관리자 전용)
   *
   * <p>대표 사진은 WebP 포맷으로 변환되어 S3에 저장됩니다.
   *
   * @param request 행사 기본 정보 (행사명, 설명, 날짜, 장소, 상세 링크)
   * @param thumbnail 행사 대표 이미지
   * @return 생성된 행사 상세 응답
   */
  EventDetailResponse createEvent(EventInfoRequest request, MultipartFile thumbnail);

  /**
   * 기존 행사를 수정합니다. (관리자 전용)
   *
   * <p>대표 이미지를 함께 전달할 경우 기존 이미지는 삭제되고 새 이미지로 교체됩니다.
   *
   * @param id 수정할 행사 ID
   * @param request 수정할 행사 정보
   * @param thumbnail 새 대표 이미지 (선택)
   * @return 수정된 행사 상세 응답
   */
  EventDetailResponse updateEvent(Long id, EventInfoRequest request, MultipartFile thumbnail);

  /**
   * 행사를 삭제합니다. (관리자 전용)
   *
   * <p>대표 이미지 및 해당 행사에 등록된 모든 예약 이미지가 함께 삭제됩니다.
   *
   * @param id 삭제할 행사 ID
   */
  void deleteEvent(Long id);

  /* =========================
   * List
   * ========================= */

  /**
   * 예정된 행사 목록을 조회합니다.
   *
   * <p>행사 날짜 기준 오름차순으로 정렬되며, D-Day 정보가 포함된 카드 형태의 응답을 반환합니다.
   *
   * @return 예정된 행사 카드 응답 리스트
   */
  List<EventCardResponse> getUpcomingEvents();

  /**
   * 종료된 행사 목록을 조회합니다.
   *
   * <p>행사 날짜 기준 내림차순으로 정렬되며, 간단한 행사 정보 형태의 응답을 반환합니다.
   *
   * @return 종료된 행사 간단 응답 리스트
   */
  List<EventSimpleResponse> getEndedEvents();

  /**
   * 행사 목록을 그룹 형태로 조회합니다.
   *
   * <p>응답은 다음 세 그룹으로 구성됩니다.
   *
   * <ul>
   *   <li>다가오는 행사 (카드 형태, D-Day 포함)
   *   <li>예정된 행사 (간단 정보 형태)
   *   <li>종료된 행사 (간단 정보 형태)
   * </ul>
   *
   * @return 행사 그룹 응답
   */
  EventGroupResponse getEventGroups();

  /* =========================
   * Detail
   * ========================= */

  /**
   * 특정 행사에 대한 상세 정보를 조회합니다.
   *
   * <p>로그인 상태인 경우, 사용자의 해당 행사 예약 여부가 함께 반환됩니다. 비로그인 상태인 경우 예약 여부는 {@code null}로 반환됩니다.
   *
   * <p>상세 응답에는 다음 정보가 포함됩니다.
   *
   * <ul>
   *   <li>누적 예약 인원
   *   <li>대표 이미지, 행사명, 설명, 상세 링크
   *   <li>행사 날짜 및 장소
   *   <li>최근 등록된 예약 이미지 4장
   *   <li>최근 4장을 제외한 의류 수량
   * </ul>
   *
   * @param id 조회할 행사 ID
   * @return 행사 상세 응답
   */
  EventDetailResponse getEventDetail(Long id);

  /**
   * 특정 행사에 등록된 모든 예약 이미지를 조회합니다.
   *
   * <p>이미지는 최신 등록 순으로 반환되며, 페이징 없이 전체 이미지를 조회합니다.
   *
   * @param eventId 행사 ID
   * @return 행사 예약 이미지 응답 리스트
   */
  List<EventImageResponse> getEventAllReservationImages(Long eventId);

  /* =========================
   * Reservation
   * ========================= */

  /**
   * 특정 행사에 대해 예약을 진행합니다.
   *
   * <p>예약 시 다음 정보가 함께 저장됩니다.
   *
   * <ul>
   *   <li>예약자 정보 (이름, 연락처, 이메일)
   *   <li>의류 수량
   *   <li>마케팅 수신 동의 여부
   *   <li>행사에 가져올 의류 이미지 (WebP 변환 후 저장)
   * </ul>
   *
   * <p>예약이 완료되면 행사 누적 예약 수량이 증가합니다.
   *
   * @param eventId 예약할 행사 ID
   * @param request 예약 요청 정보
   * @param clothImageList 예약 시 업로드하는 의류 이미지 목록
   * @return 행사 예약 결과 응답
   */
  EventReservationResponse reserveEvent(
      Long eventId, EventRsvRequest request, List<MultipartFile> clothImageList);
}
