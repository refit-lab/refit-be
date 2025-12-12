/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sku.refit.domain.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findByDateGreaterThanEqualOrderByDateAsc(LocalDate today);

  List<Event> findByDateLessThanOrderByDateDesc(LocalDate today);

  List<Event> findByDateGreaterThanEqualOrderByDateAsc(LocalDate date, Pageable pageable);

  List<Event> findByDateLessThanOrderByDateDesc(LocalDate date, Pageable pageable);
}
