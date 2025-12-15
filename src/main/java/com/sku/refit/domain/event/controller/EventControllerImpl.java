/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.event.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.event.dto.request.EventRequest.EventInfoRequest;
import com.sku.refit.domain.event.dto.request.EventRequest.EventRsvRequest;
import com.sku.refit.domain.event.dto.response.EventResponse.EventCardResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventDetailResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventGroupResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventImageResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventPagedResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventReservationResponse;
import com.sku.refit.domain.event.dto.response.EventResponse.EventSimpleResponse;
import com.sku.refit.domain.event.entity.EventStatus;
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
      @RequestPart("request") @Valid EventInfoRequest request,
      @RequestPart("thumbnail") MultipartFile thumbnail) {

    return ResponseEntity.ok(BaseResponse.success(eventService.createEvent(request, thumbnail)));
  }

  @Override
  public ResponseEntity<BaseResponse<EventDetailResponse>> updateEvent(
      @PathVariable Long id,
      @RequestPart("request") @Valid EventInfoRequest request,
      @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {

    return ResponseEntity.ok(
        BaseResponse.success(eventService.updateEvent(id, request, thumbnail)));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deleteEvent(@PathVariable Long id) {
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
  public ResponseEntity<BaseResponse<EventDetailResponse>> getEventDetail(@PathVariable Long id) {
    return ResponseEntity.ok(BaseResponse.success(eventService.getEventDetail(id)));
  }

  @Override
  public ResponseEntity<BaseResponse<List<EventImageResponse>>> getEventAllReservationImages(
      @PathVariable Long id) {

    return ResponseEntity.ok(BaseResponse.success(eventService.getEventAllReservationImages(id)));
  }

  @Override
  public ResponseEntity<BaseResponse<EventReservationResponse>> reserveEvent(
      @PathVariable Long id,
      @RequestPart("request") @Valid EventRsvRequest request,
      @RequestPart(value = "clothImageList", required = false) List<MultipartFile> clothImageList) {

    return ResponseEntity.ok(
        BaseResponse.success(eventService.reserveEvent(id, request, clothImageList)));
  }

  @Override
  public ResponseEntity<BaseResponse<EventPagedResponse>> getEvents(
      int page, int size, EventStatus status, String q) {

    return ResponseEntity.ok(BaseResponse.success(eventService.getEvents(page, size, status, q)));
  }
}
