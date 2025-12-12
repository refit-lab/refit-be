/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.s3.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import com.sku.refit.global.config.S3Config;
import com.sku.refit.global.exception.CustomException;
import com.sku.refit.global.s3.dto.S3Response;
import com.sku.refit.global.s3.entity.PathName;
import com.sku.refit.global.s3.exception.S3ErrorStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

  private static final int WEBP_QUALITY = 90;

  private final AmazonS3 amazonS3;
  private final S3Config s3Config;

  @Override
  public S3Response uploadImage(PathName pathName, MultipartFile file) {

    String imgUrl = uploadFile(pathName, file);

    log.info("이미지 업로드 성공 - pathName: {}, imageUrl: {}", pathName, imgUrl);

    return S3Response.builder().imageUrl(imgUrl).build();
  }

  @Override
  public String uploadFile(PathName pathName, MultipartFile file) {

    validateFile(file);

    String keyName = createKeyName(pathName);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try {
      amazonS3.putObject(
          new PutObjectRequest(s3Config.getBucket(), keyName, file.getInputStream(), metadata));
      log.info("파일 업로드 성공 - keyName: {}", keyName);
      return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    } catch (Exception e) {
      log.error("S3 upload 중 오류 발생", e);
      throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
    }
  }

  @Override
  public String uploadFile(
      PathName pathName,
      Long guideId,
      InputStream inputStream,
      String originalFilename,
      String contentType) {

    String keyName = createKeyName(pathName, guideId);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(contentType);
    try {

      amazonS3.putObject(
          new PutObjectRequest(s3Config.getBucket(), keyName, inputStream, metadata));

      log.info("파일 업로드 성공 - keyName: {}", keyName);

      return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    } catch (Exception e) {
      log.error("S3 upload 중 오류 발생", e);
      throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
    }
  }

  @Override
  public String createKeyName(PathName pathName) {

    return getPrefix(pathName) + '/' + UUID.randomUUID();
  }

  @Override
  public String createKeyName(PathName pathName, Long id) {

    return getPrefix(pathName) + '/' + id.toString();
  }

  @Override
  public void deleteFile(String keyName) {

    fileExists(keyName);

    try {
      amazonS3.deleteObject(new DeleteObjectRequest(s3Config.getBucket(), keyName));
      log.info("파일 삭제 성공 - keyName: {}", keyName);
    } catch (Exception e) {
      log.error("S3 delete 중 오류 발생", e);
      throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
    }
  }

  @Override
  public List<String> getAllFiles(PathName pathName) {
    String prefix = getPrefix(pathName);
    try {
      List<String> urls =
          amazonS3
              .listObjectsV2(
                  new ListObjectsV2Request()
                      .withBucketName(s3Config.getBucket())
                      .withPrefix(prefix))
              .getObjectSummaries()
              .stream()
              .map(obj -> amazonS3.getUrl(s3Config.getBucket(), obj.getKey()).toString())
              .collect(Collectors.toList());
      log.info("파일 목록 조회 성공 - pathName: {}, 파일 수: {}", pathName, urls.size());
      return urls;
    } catch (Exception e) {
      log.error("S3 파일 목록 조회 중 오류 발생", e);
      throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
    }
  }

  @Override
  public String getFile(PathName pathName, Long id) {

    String keyName = getPrefix(pathName) + "/" + id.toString();

    fileExists(keyName);

    return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
  }

  @Override
  public void deleteFile(PathName pathName, String fileName) {
    String prefix = getPrefix(pathName);
    String keyName = prefix + "/" + fileName;
    log.info("파일 삭제 요청 - pathName: {}, fileName: {}", pathName, fileName);
    deleteFile(keyName);
  }

  @Override
  public String extractKeyNameFromUrl(String imageUrl) {

    String bucketUrl =
        "https://" + s3Config.getBucket() + ".s3." + s3Config.getRegion() + ".amazonaws.com/";
    if (!imageUrl.startsWith(bucketUrl)) {
      throw new CustomException(S3ErrorStatus.FILE_URL_INVALID);
    }
    String keyName = imageUrl.substring(bucketUrl.length());
    log.info("keyName 추출 성공 - keyName: {}", keyName);
    return keyName;
  }

  public void fileExists(String keyName) {

    if (!amazonS3.doesObjectExist(s3Config.getBucket(), keyName)) {
      throw new CustomException(S3ErrorStatus.FILE_NOT_FOUND);
    }
  }

  private void validateFile(MultipartFile file) {

    if (file.getSize() > 5 * 1024 * 1024) {
      throw new CustomException(S3ErrorStatus.FILE_SIZE_INVALID);
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new CustomException(S3ErrorStatus.FILE_TYPE_INVALID);
    }
  }

  private String getPrefix(PathName pathName) {
    return switch (pathName) {
      case PROFILE_IMAGE -> s3Config.getProfileImagePath();
      case POST -> s3Config.getPostPath();
      case CLOTH -> s3Config.getClothPath();
      case EVENT -> s3Config.getEventPath();
    };
  }

  @Override
  public String uploadFileAsWebp(PathName pathName, MultipartFile file) {

    validateFile(file);

    byte[] webpBytes = convertToWebp(file);

    String keyName = getPrefix(pathName) + "/" + UUID.randomUUID() + ".webp";

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(webpBytes.length);
    metadata.setContentType("image/webp");

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(webpBytes)) {
      amazonS3.putObject(
          new PutObjectRequest(s3Config.getBucket(), keyName, inputStream, metadata));

      log.info("파일(WebP) 업로드 성공 - bucket: {}, keyName: {}", s3Config.getBucket(), keyName);

      return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();

    } catch (Exception e) {
      log.error("S3 WebP 업로드 중 오류 발생 - bucket: {}, keyName: {}", s3Config.getBucket(), keyName, e);
      throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
    }
  }

  private byte[] convertToWebp(MultipartFile file) {

    try {
      ImmutableImage image;
      try {
        image = ImmutableImage.loader().fromStream(file.getInputStream());
      } catch (IOException e) {
        log.error(
            "이미지 디코딩 오류 - originalFilename: {}, message: {}",
            file.getOriginalFilename(),
            e.getMessage());
        throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
      }

      if (image == null) {
        log.warn("이미지 디코딩 실패 - originalFilename: {}", file.getOriginalFilename());
        throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
      }

      WebpWriter writer = WebpWriter.DEFAULT.withQ(WEBP_QUALITY);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
        image.forWriter(writer).write(baos);
      } catch (IOException e) {
        log.error(
            "WebP 변환 중 IO 오류 - originalFilename: {}, message: {}",
            file.getOriginalFilename(),
            e.getMessage());
        throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
      }

      return baos.toByteArray();

    } catch (CustomException e) {
      throw e;

    } catch (Exception e) {
      log.error(
          "WebP 변환 중 예기치 않은 오류 - originalFilename: {}, message: {}",
          file.getOriginalFilename(),
          e.getMessage());
      throw new CustomException(S3ErrorStatus.FILE_SERVER_ERROR);
    }
  }
}
