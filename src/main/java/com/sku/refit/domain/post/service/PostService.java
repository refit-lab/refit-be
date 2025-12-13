/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.post.dto.request.PostRequest;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.global.page.response.InfiniteResponse;

/**
 * 게시글(Post) 관련 주요 기능을 제공하는 서비스 인터페이스입니다.
 *
 * <p>주요 기능:
 *
 * <ul>
 *   <li>게시글 생성
 *   <li>게시글 전체 조회
 *   <li>카테고리별 게시글 조회
 *   <li>게시글 상세 조회
 *   <li>게시글 수정
 *   <li>게시글 삭제
 * </ul>
 */
public interface PostService {

  /**
   * 새 게시글을 생성합니다.
   *
   * @param request 게시글 생성 요청 데이터
   * @param images 첨부 이미지 목록
   * @return 생성된 게시글 상세 응답
   */
  PostDetailResponse createPost(PostRequest request, List<MultipartFile> images);

  /**
   * 게시글 좋아요를 토글합니다.
   *
   * <p>이미 좋아요가 되어 있으면 취소하고, 좋아요가 없으면 새로 생성합니다.
   *
   * @param postId 게시글 ID
   * @return true: 좋아요 상태 / false: 좋아요 취소 상태
   */
  boolean togglePostLike(Long postId);

  /**
   * 모든 게시글 목록을 조회합니다.
   *
   * @return 게시글 상세 응답 리스트
   */
  List<PostDetailResponse> getAllPosts();

  /**
   * 특정 카테고리에 해당하는 게시글을 조회합니다.
   *
   * @param category 조회할 카테고리
   * @return 무한 스크롤 형태의 게시글 응답
   */
  InfiniteResponse<PostDetailResponse> getPostsByCategory(
      String category, Long lastPostId, Integer size);

  /**
   * ID로 게시글 상세 정보를 조회합니다.
   *
   * @param id 조회할 게시글 ID
   * @return 게시글 상세 응답
   */
  PostDetailResponse getPostById(Long id);

  /**
   * 게시글을 수정합니다.
   *
   * @param id 수정할 게시글 ID
   * @param request 수정 요청 데이터
   * @param images 수정할 이미지 목록
   * @return 수정된 게시글 상세 응답
   */
  PostDetailResponse updatePost(Long id, PostRequest request, List<MultipartFile> images);

  /**
   * 게시글을 삭제합니다.
   *
   * @param id 삭제할 게시글 ID
   */
  void deletePost(Long id);
}
