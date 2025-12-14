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

  List<Event> findByStartDateGreaterThanEqualOrderByStartDateAsc(LocalDate today);

  List<Event> findByEndDateLessThanOrderByEndDateDesc(LocalDate today);

  List<Event> findByStartDateGreaterThanEqualOrderByStartDateAsc(
      LocalDate today, Pageable pageable);

  List<Event> findByEndDateLessThanOrderByEndDateDesc(LocalDate today, Pageable pageable);
}
