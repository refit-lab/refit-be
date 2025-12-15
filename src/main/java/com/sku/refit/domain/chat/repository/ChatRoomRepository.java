/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sku.refit.domain.chat.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  /** 교환 게시글 기준 채팅방 존재 여부 확인 (중복 생성 방지) */
  Optional<ChatRoom> findByExchangePostIdAndSenderIdAndReceiverId(
      Long exchangePostId, Long senderId, Long receiverId);

  /** 내가 참여한 채팅방 목록 조회 - sender 이거나 receiver 인 경우 - 최신 메시지 기준 정렬 */
  @Query(
      """
select c from ChatRoom c
where (c.sender.id = :userId or c.receiver.id = :userId)
and (:lastId is null or c.id < :lastId)
order by c.lastMessageAt desc nulls last, c.id desc
""")
  List<ChatRoom> findMyChatRooms(
      @Param("userId") Long userId, @Param("lastId") Long lastId, Pageable pageable);

  /** 게시글 + 두 유저 기준 채팅방 조회 (sender/receiver 순서 상관없이) */
  @Query(
      """
    select cr
    from ChatRoom cr
    where cr.exchangePost.id = :postId
      and (
        (cr.sender.id = :userA and cr.receiver.id = :userB)
        or
        (cr.sender.id = :userB and cr.receiver.id = :userA)
      )
  """)
  Optional<ChatRoom> findByExchangePostIdAndUsers(
      @Param("postId") Long postId, @Param("userA") Long userA, @Param("userB") Long userB);
}
