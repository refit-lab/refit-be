/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.comment.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sku.refit.domain.comment.entity.Comment;
import com.sku.refit.domain.comment.entity.CommentLike;
import com.sku.refit.domain.user.entity.User;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  boolean existsByCommentAndUser(Comment comment, User user);

  Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

  long countByComment(Comment comment);

  @Query(
      """
  select cl.comment.id, count(cl)
  from CommentLike cl
  where cl.comment.id in :commentIds
  group by cl.comment.id
""")
  List<Object[]> countByCommentIds(@Param("commentIds") List<Long> commentIds);

  @Query(
      """
  select cl.comment.id
  from CommentLike cl
  where cl.comment.id in :commentIds
    and cl.user.id = :userId
""")
  Set<Long> findLikedCommentIds(
      @Param("commentIds") List<Long> commentIds, @Param("userId") Long userId);
}
