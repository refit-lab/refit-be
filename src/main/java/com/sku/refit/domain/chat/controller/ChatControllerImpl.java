/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.sku.refit.domain.chat.dto.response.ChatMessageResponse;
import com.sku.refit.domain.chat.dto.response.ChatRoomResponse;
import com.sku.refit.domain.chat.service.ChatService;
import com.sku.refit.global.page.response.InfiniteResponse;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatControllerImpl implements ChatController {

  private final ChatService chatService;

  @Override
  public ResponseEntity<BaseResponse<ChatRoomResponse>> createChatRoom(Long postId) {

    ChatRoomResponse response = chatService.createChatRoom(postId);
    return ResponseEntity.ok(BaseResponse.success(response));
  }

  @Override
  public ResponseEntity<BaseResponse<InfiniteResponse<ChatRoomResponse>>> getMyChatRooms(
      Long lastChatRoomId, Integer size) {

    return ResponseEntity.ok(
        BaseResponse.success(chatService.getMyChatRooms(lastChatRoomId, size)));
  }

  @Override
  public ResponseEntity<BaseResponse<InfiniteResponse<ChatMessageResponse>>> getMessages(
      Long roomId, Long lastChatId, Integer size) {

    return ResponseEntity.ok(
        BaseResponse.success(chatService.getMessages(roomId, lastChatId, size)));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> readMessages(Long roomId) {

    chatService.readMessages(roomId);
    return ResponseEntity.ok().build();
  }
}
