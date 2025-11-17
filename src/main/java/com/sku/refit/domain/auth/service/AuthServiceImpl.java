/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sku.refit.domain.auth.dto.request.LoginRequest;
import com.sku.refit.domain.auth.dto.response.TokenResponse;
import com.sku.refit.domain.auth.exception.AuthErrorCode;
import com.sku.refit.domain.user.entity.Role;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.domain.user.exception.UserErrorCode;
import com.sku.refit.domain.user.repository.UserRepository;
import com.sku.refit.global.exception.CustomException;
import com.sku.refit.global.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  @Value("${refit.test.username}")
  private String testUsername;

  @Value("${refit.test.password}")
  private String testPassword;

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public TokenResponse login(LoginRequest loginRequest) {

    User user =
        userRepository
            .findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword());

    authenticationManager.authenticate(authenticationToken);

    try {
      TokenResponse tokenResponse = jwtProvider.createTokens(authenticationToken);

      log.info("로그인 성공: {}", user.getUsername());
      return tokenResponse;
    } catch (Exception e) {
      throw new CustomException(AuthErrorCode.LOGIN_FAIL);
    }
  }

  @Override
  public String logout(String accessToken) {
    String username = jwtProvider.getUsernameFromToken(accessToken);

    jwtProvider.deleteRefreshToken(username);
    jwtProvider.blacklistToken(accessToken);

    log.info("로그아웃 성공: {}", username);
    return "로그아웃 성공 - 사용자: " + username;
  }

  @Override
  public String reissueAccessToken(String refreshToken) {

    String username = jwtProvider.getUsernameFromToken(refreshToken);
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    if (!jwtProvider.validateRefreshToken(user.getUsername(), refreshToken)) {
      throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    log.info("AT 재발급 성공: {}", user.getUsername());
    return jwtProvider.createToken(authentication);
  }

  @Override
  @Transactional
  public TokenResponse testLogin() {

    userRepository
        .findByUsername(testUsername)
        .orElseGet(
            () -> {
              User testUser =
                  User.builder()
                      .username(testUsername)
                      .password(new BCryptPasswordEncoder().encode(testPassword))
                      .role(Role.ROLE_USER)
                      .locationConsent(true)
                      .nickname("테스트유저")
                      .profileImageUrl("default.png")
                      .build();
              return userRepository.save(testUser);
            });

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(testUsername, testPassword);

    authenticationManager.authenticate(authenticationToken);

    try {
      TokenResponse tokenResponse = jwtProvider.createTokens(authenticationToken);

      log.info("테스트 로그인 성공: {}", testUsername);
      return tokenResponse;
    } catch (Exception e) {
      throw new CustomException(AuthErrorCode.LOGIN_FAIL);
    }
  }
}
