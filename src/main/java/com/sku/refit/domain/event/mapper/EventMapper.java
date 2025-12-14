/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.mapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.sku.refit.domain.event.dto.request.EventRequest.*;
import com.sku.refit.domain.event.dto.response.EventResponse.*;
import com.sku.refit.domain.event.entity.Event;
import com.sku.refit.domain.event.entity.EventReservation;
import com.sku.refit.domain.event.entity.EventReservationImage;
import com.sku.refit.domain.event.entity.EventStatus;
import com.sku.refit.domain.user.entity.User;

@Component
public class EventMapper {

  public Event toEvent(EventInfoRequest req, String thumbnailUrl) {
    return Event.builder()
        .name(req.getName())
        .description(req.getDescription())
        .startDate(req.getStartDate())
        .endDate(req.getEndDate())
        .location(req.getLocation())
        .detailLink(req.getDetailLink())
        .thumbnailUrl(thumbnailUrl)
        .capacity(req.getCapacity()) // null 허용
        .totalReservedCount(0)
        .build();
  }

  public EventDetailResponse toDetail(
      Event event, Boolean isReserved, List<String> recent4, int clothCountExcept4) {
    return EventDetailResponse.builder()
        .isReserved(isReserved)
        .eventId(event.getId())
        .totalReservedCount(event.getTotalReservedCount())
        .thumbnailUrl(event.getThumbnailUrl())
        .name(event.getName())
        .description(event.getDescription())
        .detailLink(event.getDetailLink())
        .startDate(event.getStartDate())
        .endDate(event.getEndDate())
        .location(event.getLocation())
        .capacity(event.getCapacity())
        .recentImageUrlList(recent4)
        .clothCountExceptRecent4(clothCountExcept4)
        .build();
  }

  public EventImageResponse toImageResponse(EventReservationImage img) {
    return EventImageResponse.builder().order(img.getId()).imageUrl(img.getImageUrl()).build();
  }

  /* =========================
   * List Responses
   * ========================= */

  public EventCardResponse toUpcomingCard(Event event, LocalDate today) {
    long dday = ChronoUnit.DAYS.between(today, event.getStartDate());

    return EventCardResponse.builder()
        .eventId(event.getId())
        .thumbnailUrl(event.getThumbnailUrl())
        .dday(dday)
        .name(event.getName())
        .description(event.getDescription())
        .location(event.getLocation())
        .build();
  }

  public EventSimpleResponse toSimple(Event event) {
    return EventSimpleResponse.builder()
        .eventId(event.getId())
        .thumbnailUrl(event.getThumbnailUrl())
        .name(event.getName())
        .startDate(event.getStartDate()) // DTO 스펙에 맞춤
        .location(event.getLocation())
        .build();
  }

  public List<EventCardResponse> toUpcomingCardList(List<Event> events, LocalDate today) {
    return events.stream().map(e -> toUpcomingCard(e, today)).toList();
  }

  public List<EventSimpleResponse> toSimpleList(List<Event> events) {
    return events.stream().map(this::toSimple).toList();
  }

  public EventGroupResponse toGroupResponseSingle(
      EventCardResponse upcoming, EventSimpleResponse scheduled, EventSimpleResponse ended) {
    return EventGroupResponse.builder()
        .upcoming(upcoming)
        .scheduled(scheduled)
        .ended(ended)
        .build();
  }

  /* =========================
   * Reservation Response
   * ========================= */

  public EventReservation toReservation(Event event, User user, EventRsvRequest request) {
    return EventReservation.builder()
        .event(event)
        .user(user)
        .name(request.getName())
        .phone(request.getPhone())
        .email(request.getEmail())
        .clothCount(request.getClothCount() == null ? 0 : request.getClothCount())
        .marketingConsent(request.getMarketingConsent())
        .build();
  }

  public EventReservationResponse toReservationResponse(Event event) {
    return EventReservationResponse.builder()
        .eventId(event.getId())
        .reserved(true)
        .totalReservedCount(event.getTotalReservedCount())
        .build();
  }

  public EventPagedResponse toPagedResponse(Page<Event> page, LocalDate today) {

    List<EventListItem> items =
        page.getContent().stream().map(event -> toItem(event, today)).toList();

    return EventPagedResponse.builder()
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .hasNext(page.hasNext())
        .items(items)
        .build();
  }

  private EventListItem toItem(Event event, LocalDate today) {

    return EventListItem.builder()
        .eventId(event.getId())
        .name(event.getName())
        .startDate(event.getStartDate())
        .location(event.getLocation())
        .reservedCount(event.getTotalReservedCount() == null ? 0 : event.getTotalReservedCount())
        .capacity(event.getCapacity())
        .status(resolveStatus(event, today))
        .build();
  }

  private EventStatus resolveStatus(Event event, LocalDate today) {

    if (event.getStartDate().isAfter(today)) {
      return EventStatus.UPCOMING;
    }

    if (event.getEndDate().isBefore(today)) {
      return EventStatus.ENDED;
    }

    return EventStatus.ONGOING;
  }
}
