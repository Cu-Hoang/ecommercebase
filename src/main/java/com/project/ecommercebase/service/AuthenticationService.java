package com.project.ecommercebase.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.dto.request.LoginRequest;
import com.project.ecommercebase.dto.request.RefreshTokenRequest;

public interface AuthenticationService {
    Map<String, String> login(LoginRequest loginRequest, String userAgent);

    String generateAccessToken(User user, String userAgent, String jwtID);

    Map<String, String> generateNewToken(
            HttpServletRequest httpServletRequest, RefreshTokenRequest refreshTokenRequest);

    Boolean verifyRefreshToken(LoginRequest loginRequest, String token);

    void refreshAccessToken(LoginRequest loginRequest, String token);

    void logout(LoginRequest loginRequest);

    void logoutAll(LoginRequest loginRequest);
}
