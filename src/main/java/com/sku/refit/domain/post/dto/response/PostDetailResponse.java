/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.sku.refit.domain.post.entity.PostCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "PostDetailResponse DTO", description = "게시글 상세 정보 응답 반환")
public class PostDetailResponse {

  @Schema(description = "카테고리", example = "FREE")
  private PostCategory category;

  @Schema(description = "게시글 식별자", example = "1")
  private Long postId;

  @Schema(description = "게시글 제목", example = "21파티에선 정확히 어떤걸 하나요?")
  private String title;

  @Schema(description = "게시글 내용", example = "처음 가보는거라 질문 드립니다 ㅠㅠ")
  private String content;

  @Schema(description = "게시글 조회수", example = "100")
  private Long views;

  @Schema(description = "게시글 좋아요수", example = "100")
  private Long likes;

  @Schema(description = "게시글 댓글수", example = "100")
  private Long comments;

  @Schema(description = "게시글 작성 시간", example = "2025-12-03T14:37:17")
  private LocalDateTime createdAt;

  @Schema(description = "게시글 작성자", example = "김다입")
  private String nickname;

  @Schema(description = "작성자 본인 여부", example = "true")
  private Boolean isAuthor;

  @Schema(description = "내가 좋아요를 눌렀는지 여부", example = "true")
  private Boolean isLiked;

  @Schema(description = "이미지 URL 리스트")
  private List<String> imageUrlList;

  @Schema(description = "댓글 식별자 리스트", example = "[1, 2, 3]")
  private List<Long> commentIdList;
}
