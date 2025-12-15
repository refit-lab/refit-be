/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.controller;

/*
 * Copyright (c) SKU 다시입을Lab
 */

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sku.refit.domain.chat.dto.response.ChatMessageResponse;
import com.sku.refit.domain.chat.dto.response.ChatRoomResponse;
import com.sku.refit.global.page.response.InfiniteResponse;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "채팅", description = "채팅 관련 API")
@RequestMapping("/api/chats")
public interface ChatController {

  @PostMapping("/exchange/{postId}")
  @Operation(summary = "새 채팅방 생성", description = "특정 교환 게시글의 채팅방을 생성합니다.")
  ResponseEntity<BaseResponse<ChatRoomResponse>> createChatRoom(
      @Parameter(description = "채팅방을 생성할 교환글 식별자", example = "1") @PathVariable Long postId);

  @GetMapping("/rooms")
  @Operation(summary = "채팅방 조회", description = "사용자의 채팅방 내역을 조회합니다.")
  ResponseEntity<BaseResponse<InfiniteResponse<ChatRoomResponse>>> getMyChatRooms(
      @Parameter(description = "마지막으로 조회한 채팅방 식별자(첫 조회 시 생략)", example = "5")
          @RequestParam(required = false)
          Long lastChatRoomId,
      @Parameter(description = "한 번에 조회할 채팅방 개수", example = "5") @RequestParam(defaultValue = "5")
          Integer size);

  @GetMapping("/rooms/{roomId}/messages")
  ResponseEntity<BaseResponse<InfiniteResponse<ChatMessageResponse>>> getMessages(
      @Parameter(description = "채팅방 식별자", example = "1") @PathVariable Long roomId,
      @Parameter(description = "마지막으로 조회한 채팅 식별자(첫 조회 시 생략)", example = "10")
          @RequestParam(required = false)
          Long lastChatId,
      @Parameter(description = "한 번에 조회할 채팅 개수", example = "10") @RequestParam(defaultValue = "10")
          Integer size);

  @PutMapping("/rooms/{roomId}/read")
  ResponseEntity<BaseResponse<Void>> readMessages(
      @Parameter(description = "채팅방 식별자", example = "1") @PathVariable Long roomId);
}
