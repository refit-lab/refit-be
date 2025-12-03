/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.post.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.sku.refit.domain.comment.entity.Comment;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "post")
public class Post extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @ElementCollection
  @CollectionTable(name = "post_category", joinColumns = @JoinColumn(name = "post_id"))
  @Column(nullable = false)
  private List<String> categoryList;

  @ElementCollection
  @CollectionTable(name = "post_image_url", joinColumns = @JoinColumn(name = "post_id"))
  @Column(name = "image_url", nullable = false)
  private List<String> imageUrlList = new ArrayList<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(
      mappedBy = "post",
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @Builder.Default
  private List<Comment> commentList = new ArrayList<>();

  public void update(String title, String content, List<String> imageUrlList) {
    this.title = title;
    this.content = content;
    this.imageUrlList = imageUrlList;
  }
}
