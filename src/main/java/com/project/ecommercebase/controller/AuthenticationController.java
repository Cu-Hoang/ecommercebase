package com.project.ecommercebase.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.*;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoint.ENDPOINT_AUTH)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping(value = Endpoint.AuthEndpoint.LOGIN_PASSWORD)
    public ApiResponse<Map<String, String>> loginWithPassword(
            HttpServletRequest request, @RequestBody @Validated LoginRequest loginRequest) {
        return ApiResponse.<Map<String, String>>builder()
                .data(authenticationService.loginWithPassword(loginRequest, request.getHeader("User-Agent")))
                .build();
    }

    @PostMapping(value = Endpoint.AuthEndpoint.GENERATE_NEW_TOKEN)
    public ApiResponse<Map<String, String>> generateNewToken(
            HttpServletRequest request, @RequestBody @Validated RefreshTokenRequest refreshTokenRequest) {
        return ApiResponse.<Map<String, String>>builder()
                .data(authenticationService.generateNewToken(request, refreshTokenRequest))
                .build();
    }

    @PostMapping(value = Endpoint.AuthEndpoint.LOGOUT)
    public ApiResponse<String> logout(HttpServletRequest request) {
        return ApiResponse.<String>builder()
                .message(authenticationService.logout(request))
                .build();
    }

    @PostMapping(value = Endpoint.AuthEndpoint.LOGOUT_ALL_DEVICES)
    public ApiResponse<String> logoutAllDevices(HttpServletRequest request) {
        return ApiResponse.<String>builder()
                .message(authenticationService.logoutAllDevices(request))
                .build();
    }

    @PostMapping(value = Endpoint.AuthEndpoint.EMAIL_OTP_PASSWORD)
    public ApiResponse<String> createEmailOtpPassword(@RequestBody @Validated EmailRequest emailRequest) {
        return ApiResponse.<String>builder()
                .message(authenticationService.createEmailOtpPassword(emailRequest))
                .build();
    }

    @PostMapping(value = Endpoint.AuthEndpoint.LOGIN_OTP)
    public ApiResponse<Map<String, String>> loginWithOTP(
            HttpServletRequest request, @RequestBody @Validated LoginOtpRequest loginOtpRequest) {
        return ApiResponse.<Map<String, String>>builder()
                .data(authenticationService.loginWithOTP(loginOtpRequest, request.getHeader("User-Agent")))
                .build();
    }

    @PostMapping(value = Endpoint.AuthEndpoint.EMAIL_OTP_RESET_PASSWORD)
    public ApiResponse<String> createEmailOtpResetPassword(@RequestBody @Validated EmailRequest emailRequest) {
        return ApiResponse.<String>builder()
                .message(authenticationService.createEmailOtpResetPassword(emailRequest))
                .build();
    }

    @PostMapping(value = Endpoint.AuthEndpoint.RESET_PASSWORD)
    public ApiResponse<String> resetPassword(@RequestBody @Validated ResetPasswordRequest resetPasswordRequest) {
        return ApiResponse.<String>builder()
                .message(authenticationService.resetPassword(resetPasswordRequest))
                .build();
    }
}
