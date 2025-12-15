/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.chat.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "ChatRoomResponse DTO", description = "채팅방 응답 반환")
public class ChatRoomResponse {

  @Schema(description = "채팅방 식별자", example = "1")
  private Long roomId;

  @Schema(description = "교환글 식별자", example = "1")
  private Long exchangePostId;

  @Schema(description = "수신자 닉네임", example = "김재생")
  private String receiverNickname;

  @Schema(description = "마지막 메세지", example = "내일 오후 1시에 가능합니다!")
  private String lastMessage;

  @Schema(description = "마지막 메세지 작성 시간", example = "20250101T120000")
  private LocalDateTime lastMessageAt;
}
