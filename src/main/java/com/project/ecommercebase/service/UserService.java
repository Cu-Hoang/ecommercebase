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

    String updateToVendor();

    String createShop(ShopRequest shopRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(UUID id);

    UserResponse updateUser(UserUpdateRequest userUpdateRequest);

    String updatePassword(UpdatePasswordRequest updatePasswordRequest);
}
