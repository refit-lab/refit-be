/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.user.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sku.refit.domain.user.dto.response.UserDetailResponse;
import com.sku.refit.domain.user.entity.User;

public interface UserService {

  Boolean checkNicknameAvailability(String nickname);

  List<UserDetailResponse> getAllUsers();

  UserDetailResponse getUserDetail();

  String getUserNickname();

  String updateNickname(String newNickname);

  String updateProfileImage(MultipartFile profileImage);

  boolean toggleLocationConsent();

  void deleteUser();

  User getCurrentUser();
}
