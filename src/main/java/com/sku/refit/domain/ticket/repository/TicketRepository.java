/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sku.refit.domain.ticket.entity.Ticket;
import com.sku.refit.domain.ticket.entity.TicketType;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

  Optional<Ticket> findByToken(String token);

  List<Ticket> findAllByUserIdAndTypeOrderByCreatedAtDesc(Long userId, TicketType type);

  List<Ticket> findAllByUserIdAndTypeAndUsedAtIsNotNullOrderByUsedAtDesc(
      Long userId, TicketType type);
}
