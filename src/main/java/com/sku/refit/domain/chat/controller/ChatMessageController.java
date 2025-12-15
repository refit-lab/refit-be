/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.sku.refit.domain.chat.dto.request.ChatMessageRequest;
import com.sku.refit.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

  private final ChatService chatService;

  @MessageMapping("/chat/send")
  public void sendMessage(ChatMessageRequest request, Principal principal) {

    log.info(
        "[WS CONTROLLER] sendMessage 호출됨 roomId={}, principal={}",
        request.getRoomId(),
        principal != null ? principal.getName() : "null");
    chatService.sendMessage(request, principal);
  }
}
