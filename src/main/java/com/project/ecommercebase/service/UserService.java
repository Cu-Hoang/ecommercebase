package com.project.ecommercebase.service;

import java.util.List;
import java.util.UUID;

import com.project.ecommercebase.dto.request.*;
import com.project.ecommercebase.dto.response.UserResponse;
import com.project.ecommercebase.enums.Role;

public interface UserService {
    String createEmailVerificationCode(EmailRequest emailRequest);

    String verifyEmail(EmailVerificationRequest emailVerificationRequest);

    UserResponse registerUser(UserRegisterRequest userRegisterRequest, Role role);

    String updateToVendor(EmailRequest emailRequest);

    String updateShop(UUID id, UpdateShopRequest updateShopRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(UUID id);

    UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest);

    String updatePassword(UUID id, UpdatePasswordRequest updatePasswordRequest);
}
