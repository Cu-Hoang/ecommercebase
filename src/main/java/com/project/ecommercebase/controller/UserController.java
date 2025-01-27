package com.project.ecommercebase.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.*;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.dto.response.UserResponse;
import com.project.ecommercebase.enums.Role;
import com.project.ecommercebase.service.UserService;

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
    UserService userService;

    @PostMapping(value = Endpoint.UserEndpoint.REGISTER)
    public ApiResponse<String> createEmailVerificationCode(@RequestBody @Validated EmailRequest emailRequest) {
        return ApiResponse.<String>builder()
                .message(userService.createEmailVerificationCode(emailRequest))
                .build();
    }

    @PostMapping(value = Endpoint.UserEndpoint.VERIFY_EMAIL_CODE)
    public ApiResponse<String> checkEmailVerificationCode(
            @RequestBody @Validated EmailVerificationRequest emailVerificationRequest) {
        return ApiResponse.<String>builder()
                .message(userService.verifyEmail(emailVerificationRequest))
                .build();
    }

    @PostMapping(value = Endpoint.UserEndpoint.REGISTER_CUSTOMER)
    public ApiResponse<UserResponse> registerCustomer(@RequestBody @Validated UserRegisterRequest userRegisterRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("You have registered a customer successfully.")
                .data(userService.registerUser(userRegisterRequest, Role.CUSTOMER))
                .build();
    }

    @PostMapping(value = Endpoint.UserEndpoint.REGISTER_VENDOR)
    public ApiResponse<UserResponse> registerVendor(@RequestBody @Validated UserRegisterRequest userRegisterRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("You have registered a vendor successfully.")
                .data(userService.registerUser(userRegisterRequest, Role.VENDOR))
                .build();
    }

    @PostMapping(value = Endpoint.UserEndpoint.UPDATE_TO_VENDOR)
    public ApiResponse<UserResponse> updateYoVendor(@RequestBody @Validated EmailRequest emailRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("The customer has just been promoted to a vendor successfully.")
                .data(userService.updateToVendor(emailRequest))
                .build();
    }

    @GetMapping(value = Endpoint.UserEndpoint.GET_BY_ID)
    public ApiResponse<UserResponse> getUserById(@PathVariable("id") UUID id) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUserById(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<UserResponse>> getAllUser() {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers())
                .build();
    }

    @PutMapping(value = Endpoint.UserEndpoint.UPDATE_USER)
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("id") UUID id, @RequestBody @Validated UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(id, userUpdateRequest))
                .build();
    }

    @PutMapping(value = Endpoint.UserEndpoint.UPDATE_USER_PASSWORD)
    public ApiResponse<String> updatePassword(
            @PathVariable("id") UUID id, @RequestBody @Validated UpdatePasswordRequest updatePasswordRequest) {
        return ApiResponse.<String>builder()
                .message(userService.updatePassword(id, updatePasswordRequest))
                .build();
    }
}
