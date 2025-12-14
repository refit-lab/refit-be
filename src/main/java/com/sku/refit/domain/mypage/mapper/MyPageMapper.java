/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.sku.refit.domain.event.entity.Event;
import com.sku.refit.domain.mypage.constant.TicketUseStatus;
import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;
import com.sku.refit.domain.mypage.entity.CarbonReductionHistory;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.domain.post.entity.Post;
import com.sku.refit.domain.ticket.entity.Ticket;
import com.sku.refit.domain.ticket.entity.TicketType;
import com.sku.refit.domain.ticket.util.TicketQrPayloadFactory;
import com.sku.refit.domain.user.dto.response.UserDetailResponse;
import com.sku.refit.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MyPageMapper {

  private final TicketQrPayloadFactory qrPayloadFactory;

  /* =========================
   * Tickets Response
   * ========================= */

  public MyTicketsResponse toMyTicketsResponse(
      Page<Ticket> ticketPage, LocalDate today, Map<Long, Event> eventMap) {

    List<MyTicketItem> items =
        ticketPage.getContent().stream()
            .map(ticket -> toMyTicketItem(ticket, today, eventMap))
            .toList();

    return MyTicketsResponse.builder()
        .page(ticketPage.getNumber())
        .size(ticketPage.getSize())
        .totalElements(ticketPage.getTotalElements())
        .totalPages(ticketPage.getTotalPages())
        .hasNext(ticketPage.hasNext())
        .items(items)
        .build();
  }

  public MyTicketItem toMyTicketItem(Ticket ticket, LocalDate today, Map<Long, Event> eventMap) {

    TicketUseStatus status = resolveStatus(ticket, today);

    String ticketName = null;
    String location = null;
    String description = null;

    if (ticket.getType() == TicketType.EVENT) {
      Event event = eventMap.get(ticket.getTargetId());
      if (event != null) {
        ticketName = event.getName();
        location = event.getLocation();
        description = event.getDescription();
      }
    } else if (ticket.getType() == TicketType.CLOTH) {
      ticketName = "의류 티켓";
    }

    return MyTicketItem.builder()
        .ticketId(ticket.getId())
        .type(ticket.getType())
        .status(status)
        .ticketName(ticketName)
        .location(location)
        .description(description)
        .url(qrPayloadFactory.create(ticket.getToken()))
        .issuedAt(ticket.getCreatedAt())
        .usedAt(ticket.getUsedAt())
        .expiresAt(ticket.getExpiresAt())
        .build();
  }

  /* =========================
   * Joined Events Response
   * ========================= */

  public JoinedEventItem toJoinedEventItem(Event event) {
    return JoinedEventItem.builder()
        .eventId(event.getId())
        .thumbnailUrl(event.getThumbnailUrl())
        .name(event.getName())
        .description(event.getDescription())
        .date(event.getStartDate())
        .location(event.getLocation())
        .build();
  }

  public JoinedEventsResponse toJoinedEventsResponse(Page<Long> eventIdPage, List<Event> events) {
    List<JoinedEventItem> items = events.stream().map(this::toJoinedEventItem).toList();

    return JoinedEventsResponse.builder()
        .page(eventIdPage.getNumber())
        .size(eventIdPage.getSize())
        .totalElements(eventIdPage.getTotalElements())
        .totalPages(eventIdPage.getTotalPages())
        .hasNext(eventIdPage.hasNext())
        .items(items)
        .build();
  }

  /* =========================
   * Home
   * ========================= */

  public CarbonReductionHistory toCarbonHistory(User user, long deltaG, LocalDateTime now) {
    return CarbonReductionHistory.builder().user(user).changedAt(now).deltaG(deltaG).build();
  }

  public CarbonChangeItem toCarbonChangeItem(CarbonReductionHistory h, Long totalAfterG) {
    return CarbonChangeItem.builder()
        .changedAt(h.getChangedAt())
        .deltaG(h.getDeltaG())
        .totalAfterG(totalAfterG)
        .build();
  }

  public MyHomeResponse toMyHomeResponse(
      User user, UserDetailResponse userDetail, List<CarbonChangeItem> carbonChangeList) {

    return MyHomeResponse.builder()
        .isLoggedIn(true)
        .user(userDetail)
        .exchangeCount(user.getExchangeCount() == null ? 0 : user.getExchangeCount())
        .totalReducedCarbonG(
            user.getTotalReducedCarbonG() == null ? 0L : user.getTotalReducedCarbonG())
        .carbonChangeList(carbonChangeList)
        .build();
  }

  /* =========================
   * Posts
   * ========================= */

  public MyPostsResponse toMyPostsResponse(Page<Post> postPage, List<PostDetailResponse> items) {
    return MyPostsResponse.builder()
        .page(postPage.getNumber())
        .size(postPage.getSize())
        .totalElements(postPage.getTotalElements())
        .totalPages(postPage.getTotalPages())
        .hasNext(postPage.hasNext())
        .items(items)
        .build();
  }

  /* =========================
   * Private
   * ========================= */

  private TicketUseStatus resolveStatus(Ticket ticket, LocalDate today) {
    if (ticket.isUsed()) {
      return TicketUseStatus.USED;
    }
    if (ticket.isExpired(today)) {
      return TicketUseStatus.EXPIRED;
    }
    return TicketUseStatus.UNUSED;
  }
}
