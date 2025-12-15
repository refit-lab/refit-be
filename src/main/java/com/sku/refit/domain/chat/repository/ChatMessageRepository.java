/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sku.refit.domain.chat.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  @Query(
      """
select m from ChatMessage m
where m.chatRoom.id = :roomId
and (:lastId is null or m.id < :lastId)
order by m.id desc
""")
  List<ChatMessage> findMessages(
      @Param("roomId") Long roomId, @Param("lastId") Long lastId, Pageable pageable);
}
