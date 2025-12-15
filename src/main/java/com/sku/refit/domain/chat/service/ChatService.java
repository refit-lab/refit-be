/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.service;

import java.security.Principal;

import com.sku.refit.domain.chat.dto.request.ChatMessageRequest;
import com.sku.refit.domain.chat.dto.response.ChatMessageResponse;
import com.sku.refit.domain.chat.dto.response.ChatRoomResponse;
import com.sku.refit.global.page.response.InfiniteResponse;

public interface ChatService {

  ChatRoomResponse createChatRoom(Long postId);

  void sendMessage(ChatMessageRequest request, Principal principal);

  InfiniteResponse<ChatRoomResponse> getMyChatRooms(Long lastChatRoomId, Integer size);

  InfiniteResponse<ChatMessageResponse> getMessages(Long roomId, Long lastChatId, Integer size);

  void readMessages(Long roomId);
}
