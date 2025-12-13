/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sku.refit.domain.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findByPostCategoryContaining(String category, Pageable pageable);

  Page<Post> findByPostCategoryContainingAndIdLessThan(
      String category, Long lastPostId, Pageable pageable);

  Page<Post> findAllByUserId(Long userId, Pageable pageable);

  Page<Post> findAllByUserIdAndIdLessThan(Long userId, Long lastPostId, Pageable pageable);
}
