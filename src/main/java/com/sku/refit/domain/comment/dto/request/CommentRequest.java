/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "CommentRequest DTO", description = "새 댓글 등록을 위한 데이터 전송")
public class CommentRequest {

  @NotBlank(message = "댓글 내용은 필수입니다.")
  @Schema(description = "댓글 내용", example = "답변 감사합니다~~~")
  private String content;

  @Schema(description = "답글을 작성할 댓글의 식별자", example = "1")
  private Long parentCommentId;
}
