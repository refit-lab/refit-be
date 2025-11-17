/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.user.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.user.dto.response.UserDetailResponse;
import com.sku.refit.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자", description = "사용자 관련 API")
@RequestMapping("/api/users")
public interface UserController {

  @GetMapping("/nickname/check")
  @Operation(
      summary = "닉네임 중복 여부 확인",
      description =
          """
              사용자가 입력한 닉네임이 이미 존재하는지 여부를 반환합니다.
              true -> 중복되는 닉네임, 변경할 수 없음.
              false -> 중복되지 않는 닉네임, 변경 가능.
              """)
  ResponseEntity<BaseResponse<Boolean>> checkNicknameDuplicated(
      @Parameter(description = "확인할 닉네임", example = "리핏") @RequestParam String nickname);

  @GetMapping("/dev")
  @Operation(summary = "[개발용]사용자 전체 조회", description = "스웨거를 사용해 전체 사용자를 조회합니다.")
  ResponseEntity<BaseResponse<List<UserDetailResponse>>> getAllUsers();

  @GetMapping
  @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
  ResponseEntity<BaseResponse<UserDetailResponse>> getUserDetail();

  @GetMapping("/nickname")
  @Operation(summary = "사용자 닉네임 조회", description = "현재 로그인한 사용자의 닉네임을 조회합니다.")
  ResponseEntity<BaseResponse<String>> getUserNickname();

  @PutMapping("/nickname")
  @Operation(summary = "닉네임 변경", description = "현재 로그인한 사용자의 닉네임을 변경합니다.")
  ResponseEntity<BaseResponse<String>> updateNickname(
      @Parameter(description = "변경할 닉네임", example = "리핏") @RequestParam String newNickname);

  @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "프로필 사진 변경", description = "현재 로그인한 사용자의 프로필 사진을 변경합니다.")
  ResponseEntity<BaseResponse<String>> updateProfileImage(
      @Parameter(description = "새로운 프로필 사진") @RequestPart MultipartFile profileImage);

  @PutMapping("/location-consent")
  @Operation(
      summary = "위치 정보 제공 동의 토글",
      description = "현재 로그인한 사용자의 위치 정보 제공 동의 여부를 반전시킵니다. (true → false, false → true)")
  ResponseEntity<BaseResponse<Boolean>> updateLocationConsent();

  @DeleteMapping
  @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 계정을 삭제합니다. (Hard Delete)")
  ResponseEntity<BaseResponse<Void>> deleteUser(HttpServletResponse response);
}
