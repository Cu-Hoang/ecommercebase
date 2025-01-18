package com.project.ecommercebase.service;

import java.util.List;
import java.util.UUID;

import com.project.ecommercebase.dto.request.EmailRequest;
import com.project.ecommercebase.dto.request.EmailVerificationRequest;
import com.project.ecommercebase.dto.request.UserRegisterRequest;
import com.project.ecommercebase.dto.request.UserUpdateRequest;
import com.project.ecommercebase.dto.response.UserResponse;
import com.project.ecommercebase.enums.Role;

public interface UserService {
    String createEmailVerificationCode(EmailRequest emailRequest);

    String verifyEmail(EmailVerificationRequest emailVerificationRequest);

    UserResponse registerUser(UserRegisterRequest userRegisterRequest, Role role);

    UserResponse updateToVendor(EmailRequest emailRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(UUID id);

    UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest);
}
