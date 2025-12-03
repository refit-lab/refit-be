/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "CommentDetailResponse DTO", description = "댓글 상세 정보 응답 반환")
public class CommentDetailResponse {

  @Schema(description = "댓글 식별자", example = "1")
  private Long commentId;

  @Schema(description = "게시글 내용", example = "처음 가보는거라 질문 드립니다 ㅠㅠ")
  private String content;

  @Schema(description = "게시글 작성자 여부", example = "true")
  private Boolean isWriter;

  @Schema(description = "게시글 작성 시간", example = "20250101T120000")
  private LocalDateTime createdAt;
}
