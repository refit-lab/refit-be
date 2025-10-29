/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.user.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.user.dto.response.UserDetailResponse;
import com.sku.refit.domain.user.service.UserService;
import com.sku.refit.global.jwt.JwtProvider;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

  private final UserService userService;
  private final JwtProvider jwtProvider;

  @Override
  public ResponseEntity<BaseResponse<Boolean>> checkNicknameDuplicated(
      @RequestParam String nickname) {
    return ResponseEntity.ok(BaseResponse.success(userService.checkNicknameAvailability(nickname)));
  }

  @Override
  public ResponseEntity<BaseResponse<List<UserDetailResponse>>> getAllUsers() {

    return ResponseEntity.ok(BaseResponse.success(userService.getAllUsers()));
  }

  @Override
  public ResponseEntity<BaseResponse<UserDetailResponse>> getUserDetail() {
    return ResponseEntity.ok(BaseResponse.success(userService.getUserDetail()));
  }

  @Override
  public ResponseEntity<BaseResponse<String>> getUserNickname() {
    return ResponseEntity.ok(BaseResponse.success(userService.getUserNickname()));
  }

  @Override
  public ResponseEntity<BaseResponse<String>> updateNickname(@RequestParam String newNickname) {
    return ResponseEntity.ok(BaseResponse.success(userService.updateNickname(newNickname)));
  }

  @Override
  public ResponseEntity<BaseResponse<String>> updateProfileImage(
      @RequestPart MultipartFile profileImage) {
    return ResponseEntity.ok(BaseResponse.success(userService.updateProfileImage(profileImage)));
  }

  @Override
  public ResponseEntity<BaseResponse<Boolean>> updateLocationConsent() {
    return ResponseEntity.ok(BaseResponse.success(userService.toggleLocationConsent()));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deleteUser(HttpServletResponse response) {
    userService.deleteUser();

    jwtProvider.removeJwtCookie(response, "accessToken");
    jwtProvider.removeJwtCookie(response, "refreshToken");

    return ResponseEntity.ok(BaseResponse.success());
  }
}
