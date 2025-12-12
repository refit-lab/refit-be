/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.event.dto.request.EventRequest.*;
import com.sku.refit.domain.event.dto.response.EventResponse.*;
import com.sku.refit.domain.event.entity.Event;
import com.sku.refit.domain.event.entity.EventReservation;
import com.sku.refit.domain.event.entity.EventReservationImage;
import com.sku.refit.domain.event.exception.EventErrorCode;
import com.sku.refit.domain.event.mapper.EventMapper;
import com.sku.refit.domain.event.repository.EventRepository;
import com.sku.refit.domain.event.repository.EventReservationImageRepository;
import com.sku.refit.domain.event.repository.EventReservationRepository;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.domain.user.service.UserService;
import com.sku.refit.global.exception.CustomException;
import com.sku.refit.global.s3.entity.PathName;
import com.sku.refit.global.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;
  private final EventReservationRepository eventReservationRepository;
  private final EventReservationImageRepository eventReservationImageRepository;

  private final S3Service s3Service;
  private final UserService userService;
  private final EventMapper eventMapper;

  /* =========================
   * Admin
   * ========================= */

  @Override
  @Transactional
  public EventDetailResponse createEvent(EventInfoRequest request, MultipartFile thumbnail) {

    if (thumbnail == null || thumbnail.isEmpty()) {
      throw new CustomException(EventErrorCode.EVENT_THUMBNAIL_REQUIRED);
    }

    String thumbnailUrl;
    try {
      thumbnailUrl = s3Service.uploadFileAsWebp(PathName.EVENT, thumbnail);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[EVENT] createEvent - thumbnail upload failed", e);
      throw new CustomException(EventErrorCode.EVENT_THUMBNAIL_UPLOAD_FAILED);
    }

    try {
      Event event = eventMapper.toEvent(request, thumbnailUrl);
      eventRepository.save(event);
      return getEventDetail(event.getId());

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[EVENT] createEvent - failed", e);
      throw new CustomException(EventErrorCode.EVENT_CREATE_FAILED);
    }
  }

  @Override
  @Transactional
  public EventDetailResponse updateEvent(
      Long id, EventInfoRequest request, MultipartFile thumbnail) {

    Event event =
        eventRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    try {
      event.update(
          request.getName(),
          request.getDescription(),
          request.getDate(),
          request.getLocation(),
          request.getDetailLink());
      if (thumbnail != null && !thumbnail.isEmpty()) {
        String oldThumbUrl = event.getThumbnailUrl();
        if (oldThumbUrl != null) {
          try {
            s3Service.deleteFile(s3Service.extractKeyNameFromUrl(oldThumbUrl));
          } catch (CustomException e) {
            throw e;
          } catch (Exception e) {
            log.error("[EVENT] updateEvent - thumbnail delete failed, eventId={}", id, e);
            throw new CustomException(EventErrorCode.EVENT_THUMBNAIL_DELETE_FAILED);
          }
        }

        try {
          String newThumbUrl = s3Service.uploadFileAsWebp(PathName.EVENT, thumbnail);
          event.updateThumbnailUrl(newThumbUrl);
        } catch (CustomException e) {
          throw e;
        } catch (Exception e) {
          log.error("[EVENT] updateEvent - thumbnail upload failed, eventId={}", id, e);
          throw new CustomException(EventErrorCode.EVENT_THUMBNAIL_UPLOAD_FAILED);
        }
      }
      return getEventDetail(event.getId());

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[EVENT] updateEvent - failed, eventId={}", id, e);
      throw new CustomException(EventErrorCode.EVENT_UPDATE_FAILED);
    }
  }

  @Override
  @Transactional
  public void deleteEvent(Long id) {

    Event event =
        eventRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    try {
      if (event.getThumbnailUrl() != null) {
        try {
          s3Service.deleteFile(s3Service.extractKeyNameFromUrl(event.getThumbnailUrl()));
        } catch (CustomException e) {
          throw e;
        } catch (Exception e) {
          log.error("[EVENT] deleteEvent - thumbnail delete failed, eventId={}", id, e);
          throw new CustomException(EventErrorCode.EVENT_THUMBNAIL_DELETE_FAILED);
        }
      }
      List<EventReservationImage> images =
          eventReservationImageRepository.findAllByReservation_Event_IdOrderByIdDesc(id);

      for (EventReservationImage img : images) {
        try {
          s3Service.deleteFile(s3Service.extractKeyNameFromUrl(img.getImageUrl()));
        } catch (CustomException e) {
          throw e;
        } catch (Exception e) {
          log.error(
              "[EVENT] deleteEvent - reservation image delete failed, eventId={}, imageId={}",
              id,
              img.getId(),
              e);
          throw new CustomException(EventErrorCode.EVENT_RESERVATION_IMAGES_DELETE_FAILED);
        }
      }

      eventReservationImageRepository.deleteAll(images);

      List<EventReservation> reservations = eventReservationRepository.findByEventId(id);
      eventReservationRepository.deleteAll(reservations);

      eventRepository.delete(event);

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[EVENT] deleteEvent - failed, eventId={}", id, e);
      throw new CustomException(EventErrorCode.EVENT_DELETE_FAILED);
    }
  }

  /* =========================
   * List
   * ========================= */

  @Override
  public List<EventCardResponse> getUpcomingEvents() {

    LocalDate today = LocalDate.now();
    List<Event> upcoming = eventRepository.findByDateGreaterThanEqualOrderByDateAsc(today);

    return eventMapper.toUpcomingCardList(upcoming, today);
  }

  @Override
  public List<EventSimpleResponse> getEndedEvents() {

    LocalDate today = LocalDate.now();
    List<Event> ended = eventRepository.findByDateLessThanOrderByDateDesc(today);

    return eventMapper.toSimpleList(ended);
  }

  @Override
  public EventGroupResponse getEventGroups() {
    LocalDate today = LocalDate.now();

    List<Event> top2Upcoming =
        eventRepository.findByDateGreaterThanEqualOrderByDateAsc(today, PageRequest.of(0, 2));

    Event upcomingEvent = top2Upcoming.size() >= 1 ? top2Upcoming.get(0) : null;
    Event scheduledEvent = top2Upcoming.size() >= 2 ? top2Upcoming.get(1) : null;

    Event endedEvent =
        eventRepository.findByDateLessThanOrderByDateDesc(today, PageRequest.of(0, 1)).stream()
            .findFirst()
            .orElse(null);

    EventCardResponse upcoming =
        (upcomingEvent == null) ? null : eventMapper.toUpcomingCard(upcomingEvent, today);

    EventSimpleResponse scheduled =
        (scheduledEvent == null) ? null : eventMapper.toSimple(scheduledEvent);

    EventSimpleResponse ended = (endedEvent == null) ? null : eventMapper.toSimple(endedEvent);

    return eventMapper.toGroupResponseSingle(upcoming, scheduled, ended);
  }

  /* =========================
   * Detail
   * ========================= */

  @Override
  public EventDetailResponse getEventDetail(Long id) {

    Event event =
        eventRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    User user = null;
    try {
      user = userService.getCurrentUser();
    } catch (Exception e) {
    }

    Boolean isReserved = null;
    if (user != null) {
      isReserved = eventReservationRepository.existsByEventIdAndUserId(id, user.getId());
    }

    List<String> recent4 =
        eventReservationImageRepository.findTop4ByReservation_Event_IdOrderByIdDesc(id).stream()
            .map(EventReservationImage::getImageUrl)
            .toList();

    int totalUploadedClothCount = eventReservationImageRepository.countByReservation_Event_Id(id);

    int clothCountExcept4 = Math.max(0, totalUploadedClothCount - 4);

    return eventMapper.toDetail(event, isReserved, recent4, clothCountExcept4);
  }

  @Override
  public List<EventImageResponse> getEventAllReservationImages(Long eventId) {

    eventRepository
        .findById(eventId)
        .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    return eventReservationImageRepository
        .findAllByReservation_Event_IdOrderByIdDesc(eventId)
        .stream()
        .map(eventMapper::toImageResponse)
        .toList();
  }

  /* =========================
   * Reservation
   * ========================= */

  @Override
  @Transactional
  public EventReservationResponse reserveEvent(
      Long eventId, EventRsvRequest request, List<MultipartFile> clothImageList) {

    Event event =
        eventRepository
            .findById(eventId)
            .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    User user = userService.getCurrentUser();

    if (eventReservationRepository.existsByEventIdAndUserId(eventId, user.getId())) {
      throw new CustomException(EventErrorCode.EVENT_ALREADY_RESERVED);
    }

    EventReservation reservation = eventMapper.toReservation(event, user, request);

    eventReservationRepository.save(reservation);

    if (clothImageList != null && !clothImageList.isEmpty()) {
      for (MultipartFile f : clothImageList) {
        try {
          String url = s3Service.uploadFileAsWebp(PathName.EVENT, f);

          eventReservationImageRepository.save(
              EventReservationImage.builder().reservation(reservation).imageUrl(url).build());
        } catch (CustomException e) {
          throw e;
        } catch (Exception e) {
          log.error("[EVENT] reserveEvent - image upload failed, eventId={}", eventId, e);
          throw new CustomException(EventErrorCode.EVENT_RESERVATION_IMAGE_UPLOAD_FAILED);
        }
      }
    }

    event.increaseReservedCount();

    return eventMapper.toReservationResponse(event);
  }
}
