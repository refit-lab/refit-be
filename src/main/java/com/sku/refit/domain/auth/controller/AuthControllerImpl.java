/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sku.refit.domain.auth.dto.request.LoginRequest;
import com.sku.refit.domain.auth.dto.response.TokenResponse;
import com.sku.refit.domain.auth.service.AuthService;
import com.sku.refit.global.jwt.JwtProvider;
import com.sku.refit.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

  private final AuthService authService;
  private final JwtProvider jwtProvider;

  @Override
  public ResponseEntity<BaseResponse<String>> login(
      HttpServletResponse response, @RequestBody @Valid LoginRequest loginRequest) {

    TokenResponse tokenResponse = authService.login(loginRequest);

    jwtProvider.addJwtToCookie(
        response,
        tokenResponse.getRefreshToken(),
        "REFRESH_TOKEN",
        jwtProvider.getExpirationTime(tokenResponse.getRefreshToken()));
    jwtProvider.addJwtToCookie(
        response,
        tokenResponse.getAccessToken(),
        "ACCESS_TOKEN",
        jwtProvider.getExpirationTime(tokenResponse.getAccessToken()));

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "로그인에 성공하였습니다.", tokenResponse.getAccessToken()));
  }

  @Override
  public ResponseEntity<BaseResponse<String>> logout(
      HttpServletRequest request, HttpServletResponse response) {

    String accessToken = jwtProvider.extractAccessToken(request);
    String result = authService.logout(accessToken);

    jwtProvider.removeJwtCookie(response, "ACCESS_TOKEN");
    jwtProvider.removeJwtCookie(response, "REFRESH_TOKEN");

    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.success(200, "로그아웃에 성공하였습니다.", result));
  }

  @Override
  public ResponseEntity<BaseResponse<String>> reissueToken(
      HttpServletRequest request, HttpServletResponse response) {

    String refreshToken = jwtProvider.extractRefreshToken(request);

    jwtProvider.validateTokenType(refreshToken, "refresh");

    String newAccessToken = authService.reissueAccessToken(refreshToken);

    jwtProvider.addJwtToCookie(
        response, newAccessToken, "ACCESS_TOKEN", jwtProvider.getExpirationTime(newAccessToken));

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "액세스 토큰 재발급에 성공하였습니다.", newAccessToken));
  }

  @Override
  public ResponseEntity<BaseResponse<String>> testLogin(HttpServletResponse response) {

    TokenResponse tokenResponse = authService.testLogin();

    jwtProvider.addJwtToCookie(
        response, tokenResponse.getRefreshToken(), "REFRESH_TOKEN", 7 * 24 * 60 * 60);
    jwtProvider.addJwtToCookie(
        response, tokenResponse.getAccessToken(), "ACCESS_TOKEN", 2 * 24 * 60 * 60);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "로그인에 성공하였습니다.", tokenResponse.getAccessToken()));
  }
}
