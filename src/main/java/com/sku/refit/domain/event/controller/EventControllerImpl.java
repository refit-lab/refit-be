/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.event.dto.request.EventRequest.*;
import com.sku.refit.domain.event.dto.response.EventResponse.*;
import com.sku.refit.domain.event.service.EventService;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventControllerImpl implements EventController {

  private final EventService eventService;

  @Override
  public ResponseEntity<BaseResponse<EventDetailResponse>> createEvent(
      @Valid EventInfoRequest request, MultipartFile thumbnail) {

    return ResponseEntity.ok(BaseResponse.success(eventService.createEvent(request, thumbnail)));
  }

  @Override
  public ResponseEntity<BaseResponse<EventDetailResponse>> updateEvent(
      Long id, @Valid EventInfoRequest request, MultipartFile thumbnail) {

    return ResponseEntity.ok(
        BaseResponse.success(eventService.updateEvent(id, request, thumbnail)));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deleteEvent(Long id) {
    eventService.deleteEvent(id);
    return ResponseEntity.ok(BaseResponse.success(null));
  }

  @Override
  public ResponseEntity<BaseResponse<List<EventCardResponse>>> getUpcomingEvents() {
    return ResponseEntity.ok(BaseResponse.success(eventService.getUpcomingEvents()));
  }

  @Override
  public ResponseEntity<BaseResponse<List<EventSimpleResponse>>> getEndedEvents() {
    return ResponseEntity.ok(BaseResponse.success(eventService.getEndedEvents()));
  }

  @Override
  public ResponseEntity<BaseResponse<EventGroupResponse>> getEventGroups() {
    return ResponseEntity.ok(BaseResponse.success(eventService.getEventGroups()));
  }

  @Override
  public ResponseEntity<BaseResponse<EventDetailResponse>> getEventDetail(Long id) {
    return ResponseEntity.ok(BaseResponse.success(eventService.getEventDetail(id)));
  }

  @Override
  public ResponseEntity<BaseResponse<List<EventImageResponse>>> getEventAllReservationImages(
      Long id) {

    return ResponseEntity.ok(BaseResponse.success(eventService.getEventAllReservationImages(id)));
  }

  @Override
  public ResponseEntity<BaseResponse<EventReservationResponse>> reserveEvent(
      Long id, @Valid EventRsvRequest request, List<MultipartFile> clothImageList) {

    return ResponseEntity.ok(
        BaseResponse.success(eventService.reserveEvent(id, request, clothImageList)));
  }
}
