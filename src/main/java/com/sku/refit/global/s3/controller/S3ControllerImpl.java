/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.s3.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.global.response.BaseResponse;
import com.sku.refit.global.s3.dto.S3Response;
import com.sku.refit.global.s3.entity.PathName;
import com.sku.refit.global.s3.service.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class S3ControllerImpl implements S3Controller {

  private final S3Service s3Service;

  @Override
  public ResponseEntity<BaseResponse<S3Response>> uploadImage(
      @RequestParam PathName pathName, MultipartFile file) {

    S3Response s3Response = s3Service.uploadImage(pathName, file);
    return ResponseEntity.ok(BaseResponse.success(s3Response));
  }

  @Override
  public ResponseEntity<BaseResponse<List<String>>> listFiles(@RequestParam PathName pathName) {
    List<String> files = s3Service.getAllFiles(pathName);
    return ResponseEntity.ok(BaseResponse.success(files));
  }

  @Override
  public ResponseEntity<BaseResponse<String>> deleteFile(
      @PathVariable PathName pathName, @PathVariable String fileName) {
    s3Service.deleteFile(pathName, fileName);
    return ResponseEntity.ok(BaseResponse.success());
  }
}
