/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sku.refit.domain.post.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

  boolean existsByPostIdAndUserId(Long postId, Long userId);

  long countByPostId(Long postId);

  @Query(
      """
  select pl.post.id, count(pl)
  from PostLike pl
  where pl.post.id in :postIds
  group by pl.post.id
""")
  List<Object[]> countByPostIds(@Param("postIds") List<Long> postIds);

  @Query(
      """
  select pl.post.id
  from PostLike pl
  where pl.post.id in :postIds
    and pl.user.id = :userId
""")
  List<Long> findLikedPostIds(@Param("postIds") List<Long> postIds, @Param("userId") Long userId);
}
