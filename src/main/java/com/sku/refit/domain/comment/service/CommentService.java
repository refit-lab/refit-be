/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.service;

import java.util.List;

import com.sku.refit.domain.comment.dto.request.CommentRequest;
import com.sku.refit.domain.comment.dto.response.CommentDetailResponse;

public interface CommentService {

  /**
   * 새로운 댓글 생성
   *
   * @param request 댓글 생성 요청 데이터
   * @param postId 댓글이 달릴 게시글 ID
   * @return 생성된 댓글 상세 정보
   */
  CommentDetailResponse createComment(CommentRequest request, Long postId);

  /**
   * 댓글 좋아요 등록
   *
   * @param commentId
   */
  void toggleLike(Long commentId);

  /**
   * 특정 게시글의 댓글 전체 조회
   *
   * @param postId 게시글 ID
   * @return 댓글 상세 정보 리스트
   */
  List<CommentDetailResponse> getAllCommentsByPostId(Long postId);

  /**
   * 댓글 수정
   *
   * @param id 댓글 ID
   * @param request 수정 요청 데이터
   * @return 수정된 댓글 정보
   */
  CommentDetailResponse updateComment(Long id, CommentRequest request);

  /**
   * 댓글 삭제 (Hard Delete)
   *
   * @param id 댓글 ID
   */
  void deleteComment(Long id);
}
