/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "PostRequest DTO", description = "새 게시물 등록을 위한 데이터 전송")
public class PostRequest {

  @NotEmpty(message = "게시글 카테고리는 필수입니다.")
  @Schema(description = "게시글 카테고리")
  private List<String> categoryList;

  @NotBlank(message = "게시글 제목은 필수입니다.")
  @Schema(description = "게시글 제목", example = "21파티에선 정확히 어떤걸 하나요?")
  private String title;

  @NotBlank(message = "게시글 내용은 필수입니다.")
  @Schema(description = "게시글 내용", example = "처음 가보는거라 질문 드립니다 ㅠㅠ")
  private String content;
}
