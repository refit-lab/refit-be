/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sku.refit.domain.exchange.dto.request.ExchangePostRequest;
import com.sku.refit.domain.exchange.dto.response.ExchangePostCardResponse;
import com.sku.refit.domain.exchange.dto.response.ExchangePostDetailResponse;
import com.sku.refit.domain.exchange.entity.ClothSize;
import com.sku.refit.domain.exchange.entity.ClothStatus;
import com.sku.refit.domain.exchange.entity.ExchangeCategory;
import com.sku.refit.domain.exchange.entity.ExchangePost;
import com.sku.refit.domain.user.entity.User;

@Component
public class ExchangeMapper {

  public ExchangePost toExchangePost(
      List<String> imageUrlList,
      ExchangePostRequest exchangePostRequest,
      ExchangeCategory category,
      ClothStatus status,
      ClothSize size,
      List<ExchangeCategory> preferCategoryList,
      User user) {

    return ExchangePost.builder()
        .imageUrlList(imageUrlList)
        .title(exchangePostRequest.getTitle())
        .exchangeCategory(category)
        .clothStatus(status)
        .clothSize(size)
        .description(exchangePostRequest.getDescription())
        .preferCategories(preferCategoryList)
        .exchangeSpot(exchangePostRequest.getExchangeSpot())
        .spotLatitude(exchangePostRequest.getSpotLatitude())
        .spotLongitude(exchangePostRequest.getSpotLongitude())
        .letter(exchangePostRequest.getLetter())
        .user(user)
        .build();
  }

  public ExchangePostDetailResponse toDetailResponse(ExchangePost exchangePost, User user) {

    return ExchangePostDetailResponse.builder()
        .exchangePostId(exchangePost.getId())
        .nickname(exchangePost.getUser().getNickname())
        .imageUrlList(exchangePost.getImageUrlList())
        .category(exchangePost.getExchangeCategory())
        .title(exchangePost.getTitle())
        .size(exchangePost.getClothSize())
        .status(exchangePost.getClothStatus())
        .preferCategoryList(exchangePost.getPreferCategories())
        .exchangeSpot(exchangePost.getExchangeSpot())
        .spotLatitude(exchangePost.getSpotLatitude())
        .spotLongitude(exchangePost.getSpotLongitude())
        .isAuthor(exchangePost.getUser().getId().equals(user.getId()))
        .createdAt(exchangePost.getCreatedAt())
        .build();
  }

  public ExchangePostCardResponse toCardResponse(ExchangePost exchangePost) {
    return ExchangePostCardResponse.builder()
        .exchangePostId(exchangePost.getId())
        .thumbnailImageUrl(exchangePost.getImageUrlList().getFirst())
        .category(exchangePost.getExchangeCategory())
        .title(exchangePost.getTitle())
        .exchangeSpot(exchangePost.getExchangeSpot())
        .build();
  }
}
