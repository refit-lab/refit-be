/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "CommentDetailResponse DTO", description = "댓글 상세 정보 응답 반환")
public class CommentDetailResponse {

  @Schema(description = "댓글 식별자", example = "1")
  private Long commentId;

  @Schema(description = "댓글 내용", example = "처음 가보는거라 질문 드립니다 ㅠㅠ")
  private String content;

  @Schema(description = "댓글 작성자", example = "김다입")
  private String nickname;

  @Schema(description = "댓글 작성자 프로필사진")
  private String profileImageUrl;

  @Schema(description = "댓글 작성자 여부", example = "true")
  private Boolean isWriter;

  @Schema(description = "댓글 작성 시간", example = "20250101T120000")
  private LocalDateTime createdAt;

  @Schema(description = "부모 댓글 ID (null이면 일반 댓글)", example = "null")
  private Long parentCommentId;

  @Schema(description = "좋아요 개수", example = "3")
  private Long likeCount;

  @Schema(description = "내가 좋아요를 눌렀는지 여부", example = "true")
  private Boolean isLiked;

  @Schema(description = "답글 리스트")
  private List<CommentDetailResponse> replies;
}
