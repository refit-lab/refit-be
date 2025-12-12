/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sku.refit.domain.event.entity.EventReservationImage;

public interface EventReservationImageRepository
    extends JpaRepository<EventReservationImage, Long> {

  // 행사(eventId)에 속한 "모든 예약 이미지"를 최신 등록순(id desc)으로
  @Query(
      """
      select eri
      from EventReservationImage eri
      join eri.reservation r
      where r.event.id = :eventId
      order by eri.id desc
  """)
  List<EventReservationImage> findAllByEventIdOrderByIdDesc(Long eventId);

  // 최근 4장
  @Query(
      """
      select eri
      from EventReservationImage eri
      join eri.reservation r
      where r.event.id = :eventId
      order by eri.id desc
  """)
  List<EventReservationImage> findTop4ByEventIdOrderByIdDesc(Long eventId, Pageable pageable);
}
