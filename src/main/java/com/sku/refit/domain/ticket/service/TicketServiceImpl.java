/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sku.refit.domain.ticket.dto.request.TicketRequest.*;
import com.sku.refit.domain.ticket.dto.response.TicketResponse.*;
import com.sku.refit.domain.ticket.entity.Ticket;
import com.sku.refit.domain.ticket.entity.TicketType;
import com.sku.refit.domain.ticket.exception.TicketErrorCode;
import com.sku.refit.domain.ticket.mapper.TicketMapper;
import com.sku.refit.domain.ticket.repository.TicketRepository;
import com.sku.refit.domain.ticket.util.TicketTokenGenerator;
import com.sku.refit.domain.user.service.UserService;
import com.sku.refit.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

  private final TicketRepository ticketRepository;
  private final UserService userService;

  private final TicketMapper ticketMapper;
  private final TicketTokenGenerator tokenGenerator;

  /* =========================
   * Issue
   * ========================= */

  @Override
  @Transactional
  public TicketDetailResponse issueTicket(TicketType type, Long targetId, Long userId) {

    if (type == null || targetId == null) {
      throw new CustomException(TicketErrorCode.TICKET_BAD_REQUEST);
    }

    Long issueUserId;
    if (userId != null) {
      issueUserId = userId;
    } else {
      issueUserId = userService.getCurrentUser().getId();
    }

    String token;
    try {
      token = tokenGenerator.generate();
    } catch (Exception e) {
      log.error("[TICKET] issueTicket - token generation failed, userId={}", issueUserId, e);
      throw new CustomException(TicketErrorCode.TICKET_TOKEN_GENERATION_FAILED);
    }

    try {
      Ticket ticket = ticketMapper.toEntity(type, targetId, issueUserId, token);
      Ticket saved = ticketRepository.save(ticket);
      return ticketMapper.toDetail(saved);

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error(
          "[TICKET] issueTicket - save failed, userId={}, type={}, targetId={}",
          issueUserId,
          type,
          targetId,
          e);
      throw new CustomException(TicketErrorCode.TICKET_ISSUE_FAILED);
    }
  }

  /* =========================
   * Verify
   * ========================= */

  @Override
  public VerifyTicketResponse verifyTicket(VerifyTicketRequest request) {

    validateToken(request.getToken());

    try {
      return ticketRepository
          .findByTokenForUpdate(request.getToken())
          .map(ticketMapper::toVerifyFound)
          .orElseGet(ticketMapper::toVerifyNotFound);

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[TICKET] verifyTicket - failed", e);
      throw new CustomException(TicketErrorCode.TICKET_VERIFY_FAILED);
    }
  }

  /* =========================
   * Consume
   * ========================= */

  @Override
  @Transactional
  public ConsumeTicketResponse consumeTicket(ConsumeTicketRequest request) {

    validateToken(request.getToken());

    Ticket ticket =
        ticketRepository
            .findByToken(request.getToken())
            .orElseThrow(() -> new CustomException(TicketErrorCode.TICKET_NOT_FOUND));

    try {
      boolean alreadyUsed = ticket.isUsed();

      if (!alreadyUsed) {
        ticket.consume(LocalDateTime.now());
      }

      return ticketMapper.toConsume(ticket, !alreadyUsed);

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error(
          "[TICKET] consumeTicket - failed, ticketId={}, type={}, targetId={}",
          ticket.getId(),
          ticket.getType(),
          ticket.getTargetId(),
          e);
      throw new CustomException(TicketErrorCode.TICKET_CONSUME_FAILED);
    }
  }

  /* =========================
   * My Tickets
   * ========================= */

  @Override
  public List<MyTicketItemResponse> getMyTicketsUsedEvents() {

    Long userId = userService.getCurrentUser().getId();

    try {
      return ticketRepository
          .findAllByUserIdAndTypeAndUsedAtIsNotNullOrderByUsedAtDesc(userId, TicketType.EVENT)
          .stream()
          .map(ticketMapper::toMyItem)
          .toList();

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[TICKET] getMyTicketsUsedEvents - failed, userId={}", userId, e);
      throw new CustomException(TicketErrorCode.TICKET_MY_LIST_FAILED);
    }
  }

  @Override
  public List<MyTicketItemResponse> getMyClothTickets() {

    Long userId = userService.getCurrentUser().getId();

    try {
      return ticketRepository
          .findAllByUserIdAndTypeOrderByCreatedAtDesc(userId, TicketType.CLOTH)
          .stream()
          .map(ticketMapper::toMyItem)
          .toList();

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[TICKET] getMyClothTickets - failed, userId={}", userId, e);
      throw new CustomException(TicketErrorCode.TICKET_MY_LIST_FAILED);
    }
  }

  /* =========================
   * Private
   * ========================= */

  private void validateIssueRequest(IssueTicketRequest request) {
    if (request == null || request.getType() == null || request.getTargetId() == null) {
      throw new CustomException(TicketErrorCode.TICKET_BAD_REQUEST);
    }
  }

  private void validateToken(String token) {
    if (token == null || token.isBlank()) {
      throw new CustomException(TicketErrorCode.TICKET_TOKEN_REQUIRED);
    }
  }
}
