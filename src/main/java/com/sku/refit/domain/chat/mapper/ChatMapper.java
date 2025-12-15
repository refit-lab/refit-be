/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.mapper;

import org.springframework.stereotype.Component;

import com.sku.refit.domain.chat.dto.response.ChatMessageResponse;
import com.sku.refit.domain.chat.dto.response.ChatRoomResponse;
import com.sku.refit.domain.chat.entity.ChatMessage;
import com.sku.refit.domain.chat.entity.ChatRoom;
import com.sku.refit.domain.exchange.entity.ExchangePost;
import com.sku.refit.domain.user.entity.User;

@Component
public class ChatMapper {

  public ChatRoom toChatRoom(ExchangePost exchangePost, User sender, User receiver) {
    return ChatRoom.builder().exchangePost(exchangePost).sender(sender).receiver(receiver).build();
  }

  public ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom) {
    return ChatRoomResponse.builder()
        .roomId(chatRoom.getId())
        .exchangePostId(chatRoom.getExchangePost().getId())
        .receiverNickname(chatRoom.getReceiver().getNickname())
        .lastMessage(chatRoom.getLastMessage())
        .lastMessageAt(chatRoom.getLastMessageAt())
        .build();
  }

  public ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
    return ChatMessageResponse.builder()
        .messageId(chatMessage.getId())
        .roomId(chatMessage.getChatRoom().getId())
        .senderNickname(chatMessage.getSender().getNickname())
        .content(chatMessage.getContent())
        .createdAt(chatMessage.getCreatedAt())
        .build();
  }
}
