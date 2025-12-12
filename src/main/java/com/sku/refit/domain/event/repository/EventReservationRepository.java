/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sku.refit.domain.event.entity.EventReservation;

public interface EventReservationRepository extends JpaRepository<EventReservation, Long> {

  boolean existsByEventIdAndUserId(Long eventId, Long userId);

  List<EventReservation> findByEventId(Long eventId);
}
