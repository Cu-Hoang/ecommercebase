package com.project.ecommercebase.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.EmailRequest;
import com.project.ecommercebase.dto.request.EmailVerificationRequest;
import com.project.ecommercebase.dto.request.UserRegisterRequest;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.dto.response.UserResponse;
import com.project.ecommercebase.enums.Role;
import com.project.ecommercebase.service.impl.UserServiceImpl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoint.ENDPOINT_USER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserServiceImpl userService;

    @PostMapping(value = Endpoint.UserEndpoint.CREATE_USER)
    public ApiResponse<String> createEmailVerificationCode(@RequestBody @Validated EmailRequest emailRequest) {
        return ApiResponse.<String>builder()
                .message(userService.createEmailVerificationCode(emailRequest))
                .build();
    }

    @PostMapping(value = Endpoint.UserEndpoint.CHECK_CODE)
    public ApiResponse<String> checkEmailVerificationCode(
            @RequestBody @Validated EmailVerificationRequest emailVerificationRequest) {
        return ApiResponse.<String>builder()
                .message(userService.verifyEmail(emailVerificationRequest))
                .build();
    }

    @PostMapping(value = Endpoint.UserEndpoint.REGISTER_CUSTOMER)
    public ApiResponse<UserResponse> registerCustomer(@RequestBody @Validated UserRegisterRequest userRegisterRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("Customer registered successfully")
                .data(userService.registerUser(userRegisterRequest, Role.CUSTOMER))
                .build();
    }

    @PostMapping(value = Endpoint.UserEndpoint.REGISTER_VENDOR)
    public ApiResponse<UserResponse> registerVendor(@RequestBody @Validated UserRegisterRequest userRegisterRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("Vendor registered successfully")
                .data(userService.registerUser(userRegisterRequest, Role.VENDOR))
                .build();
    }
}
