package com.project.ecommercebase.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.LoginRequest;
import com.project.ecommercebase.dto.request.RefreshTokenRequest;
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

    @PostMapping(value = Endpoint.AuthEndpoint.LOGIN)
    public ApiResponse<Map<String, String>> login(
            HttpServletRequest request, @RequestBody @Validated LoginRequest loginRequest) {
        return ApiResponse.<Map<String, String>>builder()
                .data(authenticationService.login(loginRequest, request.getHeader("User-Agent")))
                .build();
    }

    @PostMapping(value = Endpoint.AuthEndpoint.GENERATE_NEW_TOKEN)
    public ApiResponse<Map<String, String>> generateNewToken(
            HttpServletRequest request, @RequestBody @Validated RefreshTokenRequest refreshTokenRequest) {
        return ApiResponse.<Map<String, String>>builder()
                .data(authenticationService.generateNewToken(request, refreshTokenRequest))
                .build();
    }
}
