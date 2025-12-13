/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sku.refit.domain.ticket.entity.Ticket;
import com.sku.refit.domain.ticket.entity.TicketType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

  Optional<Ticket> findByToken(String token);

  List<Ticket> findAllByUserIdAndTypeOrderByCreatedAtDesc(Long userId, TicketType type);

  List<Ticket> findAllByUserIdAndTypeAndUsedAtIsNotNullOrderByUsedAtDesc(
      Long userId, TicketType type);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM Ticket t WHERE t.token = :token")
  Optional<Ticket> findByTokenForUpdate(@Param("token") String token);
}
