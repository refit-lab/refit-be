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
@Schema(title = "ChatMessageResponse DTO", description = "채팅 메세지 응답 반환")
public class ChatMessageResponse {

  @Schema(description = "채팅 메세지 식별자", example = "1")
  private Long messageId;

  @Schema(description = "채팅방 식별자", example = "1")
  private Long roomId;

  @Schema(description = "채팅 발신자", example = "김다입")
  private String senderNickname;

  @Schema(description = "채팅 내용", example = "안녕하세요. 교환 원하시나요?")
  private String content;

  @Schema(description = "메세지 작성 시간", example = "20250101T120000")
  private LocalDateTime createdAt;
}
