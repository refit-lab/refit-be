/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.exchange.dto.request.ExchangePostRequest;
import com.sku.refit.domain.exchange.dto.response.ExchangePostCardResponse;
import com.sku.refit.domain.exchange.dto.response.ExchangePostDetailResponse;
import com.sku.refit.domain.exchange.entity.ClothSize;
import com.sku.refit.domain.exchange.entity.ClothStatus;
import com.sku.refit.domain.exchange.entity.ExchangeCategory;
import com.sku.refit.domain.exchange.entity.ExchangePost;
import com.sku.refit.domain.exchange.entity.ExchangeStatus;
import com.sku.refit.domain.exchange.exception.ExchangeErrorCode;
import com.sku.refit.domain.exchange.mapper.ExchangeMapper;
import com.sku.refit.domain.exchange.repository.ExchangeRepository;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.domain.user.service.UserService;
import com.sku.refit.global.exception.CustomException;
import com.sku.refit.global.page.mapper.PageMapper;
import com.sku.refit.global.page.response.PageResponse;
import com.sku.refit.global.s3.entity.PathName;
import com.sku.refit.global.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {

  private final ExchangeRepository exchangeRepository;
  private final ExchangeMapper exchangeMapper;
  private final UserService userService;
  private final S3Service s3Service;
  private final PageMapper pageMapper;

  @Override
  @Transactional
  public ExchangePostDetailResponse createExchangePost(
      List<MultipartFile> imageList, ExchangePostRequest request) {

    User user = userService.getCurrentUser();
    List<String> imageUrlList = new ArrayList<>();
    if (imageList != null && !imageList.isEmpty()) {
      for (MultipartFile image : imageList) {
        String imageUrl = s3Service.uploadImage(PathName.CLOTH, image).getImageUrl();
        imageUrlList.add(imageUrl);
      }
    }

    ExchangeCategory category = ExchangeCategory.valueOf(request.getExchangeCategory());
    ClothStatus status = ClothStatus.valueOf(request.getClothStatus());
    ClothSize size = ClothSize.valueOf(request.getClothSize());
    List<ExchangeCategory> preferCategoryList =
        request.getPreferCategoryList().stream().map(ExchangeCategory::valueOf).toList();

    ExchangePost exchangePost =
        exchangeMapper.toExchangePost(
            imageUrlList, request, category, status, size, preferCategoryList, user);
    exchangeRepository.save(exchangePost);

    log.info(
        "[ExchangePost CREATE] postId={}, userId={}, imageCount={}",
        exchangePost.getId(),
        user.getId(),
        imageUrlList.size());

    return exchangeMapper.toDetailResponse(exchangePost, user);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<ExchangePostCardResponse> getExchangePostsByLocation(
      Pageable pageable, Double latitude, Double longitude) {

    Page<ExchangePost> page =
        exchangeRepository.findByDistanceAndStatus(
            latitude, longitude, ExchangeStatus.BEFORE, pageable);

    Page<ExchangePostCardResponse> mappedPage = page.map(exchangeMapper::toCardResponse);

    return pageMapper.toPageResponse(mappedPage);
  }

  @Override
  @Transactional(readOnly = true)
  public ExchangePostDetailResponse getExchangePost(Long exchangePostId) {

    User user = userService.getCurrentUser();
    ExchangePost exchangePost =
        exchangeRepository
            .findByIdAndExchangeStatus(exchangePostId, ExchangeStatus.BEFORE)
            .orElseThrow(() -> new CustomException(ExchangeErrorCode.EXCHANGE_NOT_FOUND));

    return exchangeMapper.toDetailResponse(exchangePost, user);
  }

  @Override
  @Transactional
  public ExchangePostDetailResponse updateExchangePost(
      Long exchangePostId, List<MultipartFile> imageList, ExchangePostRequest request) {

    User user = userService.getCurrentUser();

    ExchangePost exchangePost =
        exchangeRepository
            .findById(exchangePostId)
            .orElseThrow(() -> new CustomException(ExchangeErrorCode.EXCHANGE_NOT_FOUND));

    if (!exchangePost.getUser().getId().equals(user.getId())) {
      throw new CustomException(ExchangeErrorCode.EXCHANGE_ACCESS_DENIED);
    }

    if (exchangePost.getImageUrlList() != null) {
      for (String imageUrl : exchangePost.getImageUrlList()) {
        s3Service.deleteFile(s3Service.extractKeyNameFromUrl(imageUrl));
      }
    }

    List<String> newImageUrlList = new ArrayList<>();
    if (imageList != null && !imageList.isEmpty()) {
      for (MultipartFile image : imageList) {
        String imageUrl = s3Service.uploadImage(PathName.CLOTH, image).getImageUrl();
        newImageUrlList.add(imageUrl);
      }
    }

    ExchangeCategory category = ExchangeCategory.valueOf(request.getExchangeCategory());
    ClothStatus status = ClothStatus.valueOf(request.getClothStatus());
    ClothSize size = ClothSize.valueOf(request.getClothSize());
    List<ExchangeCategory> preferCategoryList =
        request.getPreferCategoryList().stream().map(ExchangeCategory::valueOf).toList();

    exchangePost.update(
        newImageUrlList,
        request.getTitle(),
        category,
        status,
        size,
        request.getDescription(),
        preferCategoryList,
        request.getExchangeSpot(),
        request.getSpotLatitude(),
        request.getSpotLongitude(),
        request.getLetter());

    log.info(
        "[ExchangePost UPDATE] postId={}, userId={}, imageCount={}",
        exchangePostId,
        user.getId(),
        newImageUrlList.size());

    return exchangeMapper.toDetailResponse(exchangePost, user);
  }

  @Override
  @Transactional
  public void deleteExchangePost(Long exchangePostId) {

    User user = userService.getCurrentUser();

    ExchangePost exchangePost =
        exchangeRepository
            .findById(exchangePostId)
            .orElseThrow(() -> new IllegalArgumentException("교환 게시글이 존재하지 않습니다."));

    // 작성자 검증
    if (!exchangePost.getUser().getId().equals(user.getId())) {
      throw new IllegalStateException("게시글 삭제 권한이 없습니다.");
    }

    // 이미지 삭제
    if (exchangePost.getImageUrlList() != null) {
      for (String imageUrl : exchangePost.getImageUrlList()) {
        s3Service.deleteFile(s3Service.extractKeyNameFromUrl(imageUrl));
      }
    }

    exchangeRepository.delete(exchangePost);

    log.info("[ExchangePost DELETE] postId={}, userId={}", exchangePostId, user.getId());
  }
}
