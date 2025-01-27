package com.project.ecommercebase.exception;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.enums.ErrorCode;

public class JwtCustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        System.out.println("JwtCustomAuthenticationEntryPoint commence() invoked");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
