package com.project.ecommercebase.exception;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.enums.ErrorCode;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        System.out.println("CustomAccessDeniedHandler commence() invoked");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
