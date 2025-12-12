/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sku.refit.domain.event.entity.EventReservationImage;

public interface EventReservationImageRepository
    extends JpaRepository<EventReservationImage, Long> {

  List<EventReservationImage> findTop4ByReservation_Event_IdOrderByIdDesc(
      Long eventId, Pageable pageable);

  int countByReservation_Event_Id(Long eventId);

  List<EventReservationImage> findAllByReservation_Event_IdOrderByIdDesc(Long eventId);
}
