package com.project.ecommercebase.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.dto.request.*;

public interface AuthenticationService {
    Map<String, String> loginWithPassword(LoginRequest loginRequest, String userAgent);

    String generateAccessToken(User user, String userAgent, String jwtID);

    Map<String, String> generateNewToken(
            HttpServletRequest httpServletRequest, RefreshTokenRequest refreshTokenRequest);

    String logout(HttpServletRequest httpServletRequest);

    String logoutAllDevices(HttpServletRequest httpServletRequest);

    String createEmailOtpPassword(EmailRequest emailRequest);

    Map<String, String> loginWithOTP(LoginOtpRequest loginOtpRequest, String userAgent);

    String createEmailOtpResetPassword(EmailRequest emailRequest);

    String resetPassword(ResetPasswordRequest resetPasswordRequest);
}
