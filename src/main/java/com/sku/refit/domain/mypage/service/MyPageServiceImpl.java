/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sku.refit.domain.event.entity.Event;
import com.sku.refit.domain.event.repository.EventRepository;
import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;
import com.sku.refit.domain.mypage.mapper.MyPageMapper;
import com.sku.refit.domain.ticket.entity.Ticket;
import com.sku.refit.domain.ticket.entity.TicketType;
import com.sku.refit.domain.ticket.repository.TicketRepository;
import com.sku.refit.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageServiceImpl implements MyPageService {

  private final UserService userService;
  private final TicketRepository ticketRepository;
  private final EventRepository eventRepository;
  private final MyPageMapper myPageMapper;

  @Override
  public MyTicketsResponse getMyTickets(int page, int size) {

    Long userId = userService.getCurrentUser().getId();
    LocalDate today = LocalDate.now();

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<Ticket> ticketPage = ticketRepository.findActiveUnusedTickets(userId, today, pageable);

    Map<Long, Event> eventMap = loadEventMap(ticketPage.getContent());

    return myPageMapper.toMyTicketsResponse(ticketPage, today, eventMap);
  }

  @Override
  public JoinedEventsResponse getJoinedEvents() {

    Long userId = userService.getCurrentUser().getId();

    List<Ticket> usedEventTickets =
        ticketRepository.findAllByUserIdAndTypeAndUsedAtIsNotNullOrderByUsedAtDesc(
            userId, TicketType.EVENT);

    if (usedEventTickets.isEmpty()) {
      return JoinedEventsResponse.builder().items(List.of()).build();
    }

    // 최신 usedAt 기준 eventId 중복 제거
    LinkedHashSet<Long> orderedEventIds = new LinkedHashSet<>();
    for (Ticket t : usedEventTickets) {
      orderedEventIds.add(t.getTargetId());
    }

    Map<Long, Event> eventMap =
        eventRepository.findAllById(orderedEventIds).stream()
            .collect(Collectors.toMap(Event::getId, Function.identity()));

    List<Event> orderedEvents =
        orderedEventIds.stream().map(eventMap::get).filter(Objects::nonNull).toList();

    return myPageMapper.toJoinedEventsResponse(orderedEvents);
  }

  private Map<Long, Event> loadEventMap(List<Ticket> tickets) {

    List<Long> eventIds =
        tickets.stream()
            .filter(t -> t.getType() == TicketType.EVENT)
            .map(Ticket::getTargetId)
            .distinct()
            .toList();

    if (eventIds.isEmpty()) {
      return Map.of();
    }

    return eventRepository.findAllById(eventIds).stream()
        .collect(Collectors.toMap(Event::getId, Function.identity()));
  }
}
