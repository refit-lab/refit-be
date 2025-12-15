/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sku.refit.domain.chat.dto.request.ChatMessageRequest;
import com.sku.refit.domain.chat.dto.response.ChatMessageResponse;
import com.sku.refit.domain.chat.dto.response.ChatRoomResponse;
import com.sku.refit.domain.chat.entity.ChatMessage;
import com.sku.refit.domain.chat.entity.ChatRoom;
import com.sku.refit.domain.chat.exception.ChatErrorCode;
import com.sku.refit.domain.chat.mapper.ChatMapper;
import com.sku.refit.domain.chat.repository.ChatMessageRepository;
import com.sku.refit.domain.chat.repository.ChatRoomRepository;
import com.sku.refit.domain.exchange.entity.ExchangePost;
import com.sku.refit.domain.exchange.exception.ExchangeErrorCode;
import com.sku.refit.domain.exchange.repository.ExchangeRepository;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.domain.user.exception.UserErrorCode;
import com.sku.refit.domain.user.repository.UserRepository;
import com.sku.refit.domain.user.service.UserService;
import com.sku.refit.global.exception.CustomException;
import com.sku.refit.global.page.mapper.InfiniteMapper;
import com.sku.refit.global.page.response.InfiniteResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

  private final SimpMessagingTemplate messagingTemplate;
  private final ChatRoomRepository chatRoomRepository;
  private final ExchangeRepository exchangeRepository;
  private final UserRepository userRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final UserService userService;
  private final ChatMapper chatMapper;
  private final InfiniteMapper infiniteMapper;

  @Override
  @Transactional
  public ChatRoomResponse createChatRoom(Long postId) {

    User user = userService.getCurrentUser();

    ExchangePost post =
        exchangeRepository
            .findById(postId)
            .orElseThrow(() -> new CustomException(ExchangeErrorCode.EXCHANGE_NOT_FOUND));

    User receiver = post.getUser();

    if (receiver == null) {
      throw new CustomException(ChatErrorCode.CHAT_NOT_FOUND);
    }

    ChatRoom room =
        chatRoomRepository
            .findByExchangePostIdAndUsers(postId, user.getId(), receiver.getId())
            .orElseGet(() -> chatRoomRepository.save(chatMapper.toChatRoom(post, user, receiver)));

    return chatMapper.toChatRoomResponse(room);
  }

  @Transactional
  public void sendMessage(ChatMessageRequest request, Principal principal) {

    if (principal == null) {
      throw new CustomException(UserErrorCode.USER_NOT_FOUND);
    }

    String username = principal.getName();
    User sender =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    ChatRoom chatRoom =
        chatRoomRepository
            .findById(request.getRoomId())
            .orElseThrow(() -> new CustomException(ChatErrorCode.CHAT_NOT_FOUND));

    ChatMessage message =
        chatMessageRepository.save(
            ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(request.getContent())
                .build());

    ChatMessageResponse response = chatMapper.toChatMessageResponse(message);

    log.info(
        "[CHAT] 메시지 수신 roomId={}, sender={}, content={}",
        request.getRoomId(),
        sender.getNickname(),
        request.getContent());

    messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatRoom.getId(), response);
  }

  @Override
  @Transactional(readOnly = true)
  public InfiniteResponse<ChatRoomResponse> getMyChatRooms(Long lastChatRoomId, Integer size) {

    User user = userService.getCurrentUser();

    Pageable pageable = PageRequest.of(0, size + 1);

    List<ChatRoom> rooms =
        chatRoomRepository.findMyChatRooms(user.getId(), lastChatRoomId, pageable);

    boolean hasNext = rooms.size() > size;

    if (hasNext) {
      rooms.remove(size);
    }

    List<ChatRoomResponse> content = rooms.stream().map(chatMapper::toChatRoomResponse).toList();

    return infiniteMapper.toInfiniteResponse(content, lastChatRoomId, hasNext, size);
  }

  @Override
  @Transactional(readOnly = true)
  public InfiniteResponse<ChatMessageResponse> getMessages(
      Long roomId, Long lastChatId, Integer size) {

    Pageable pageable = PageRequest.of(0, size + 1);

    List<ChatMessage> messages = chatMessageRepository.findMessages(roomId, lastChatId, pageable);

    boolean hasNext = messages.size() > size;

    if (hasNext) {
      messages.remove(size);
    }

    List<ChatMessageResponse> content =
        messages.stream().map(chatMapper::toChatMessageResponse).toList();

    return infiniteMapper.toInfiniteResponse(content, lastChatId, hasNext, size);
  }

  @Transactional
  @Override
  public void readMessages(Long roomId) {

    User user = userService.getCurrentUser();

    ChatRoom room =
        chatRoomRepository
            .findById(roomId)
            .orElseThrow(() -> new CustomException(ChatErrorCode.CHAT_NOT_FOUND));

    room.markAsRead(user.getId(), LocalDateTime.now());
  }
}
