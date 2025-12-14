/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sku.refit.domain.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

  List<Event> findByStartDateGreaterThanEqualOrderByStartDateAsc(LocalDate today);

  List<Event> findByEndDateLessThanOrderByEndDateDesc(LocalDate today);

  List<Event> findByStartDateGreaterThanEqualOrderByStartDateAsc(
      LocalDate today, Pageable pageable);

  List<Event> findByEndDateLessThanOrderByEndDateDesc(LocalDate today, Pageable pageable);

  /* =========================
   * Admin list (paged)
   * - status=null  : 전체(ONGOING->UPCOMING->ENDED 우선 정렬)
   * - status!=null : 필터 + (ONGOING: startDate desc / UPCOMING: startDate asc / ENDED: endDate desc)
   * ========================= */

  @Query(
      """
      select e
      from Event e
      where
        (:q is null or :q = ''
          or lower(e.name) like lower(concat('%', :q, '%'))
          or lower(e.location) like lower(concat('%', :q, '%'))
        )
      order by
        /* 1) 상태 우선순위: ONGOING(0) -> UPCOMING(1) -> ENDED(2) */
        case
          when e.startDate <= :today and e.endDate >= :today then 0
          when e.startDate > :today then 1
          else 2
        end,

        /* 2) 상태별 내부 정렬 */
        /* UPCOMING: startDate asc */
        case when e.startDate > :today then e.startDate else null end asc,

        /* ONGOING: startDate desc */
        case when e.startDate <= :today and e.endDate >= :today then e.startDate else null end desc,

        /* ENDED: endDate desc */
        case when e.endDate < :today then e.endDate else null end desc,

        /* tie-breaker */
        e.id desc
      """)
  Page<Event> findAllSortedByStatus(
      @Param("today") LocalDate today, @Param("q") String q, Pageable pageable);

  /* ===== status 별 (필터 적용 시) ===== */

  // UPCOMING: startDate > today, startDate asc
  @Query(
      """
      select e from Event e
      where e.startDate > :today
        and (:q is null or :q = ''
          or lower(e.name) like lower(concat('%', :q, '%'))
          or lower(e.location) like lower(concat('%', :q, '%'))
        )
      order by e.startDate asc, e.id desc
      """)
  Page<Event> findUpcomingSorted(
      @Param("today") LocalDate today, @Param("q") String q, Pageable pageable);

  // ONGOING: startDate <= today <= endDate, startDate desc
  @Query(
      """
      select e from Event e
      where e.startDate <= :today and e.endDate >= :today
        and (:q is null or :q = ''
          or lower(e.name) like lower(concat('%', :q, '%'))
          or lower(e.location) like lower(concat('%', :q, '%'))
        )
      order by e.startDate desc, e.id desc
      """)
  Page<Event> findOngoingSorted(
      @Param("today") LocalDate today, @Param("q") String q, Pageable pageable);

  // ENDED: endDate < today, endDate desc
  @Query(
      """
      select e from Event e
      where e.endDate < :today
        and (:q is null or :q = ''
          or lower(e.name) like lower(concat('%', :q, '%'))
          or lower(e.location) like lower(concat('%', :q, '%'))
        )
      order by e.endDate desc, e.id desc
      """)
  Page<Event> findEndedSorted(
      @Param("today") LocalDate today, @Param("q") String q, Pageable pageable);
}
