package com.project.ecommercebase.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.enums.ErrorCode;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> runtimeExceptionHandler(Exception e) {
        ErrorCode errorCode = ErrorCode.UNCLASSIFIED_EXCEPTION;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(errorCode.getMessage())
                        .code(errorCode.getCode())
                        .build());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> appExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(errorCode.getMessage())
                        .code(errorCode.getCode())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(errorCode.getMessage())
                        .code(errorCode.getCode())
                        .data(errors)
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(errorCode.getMessage())
                        .code(errorCode.getCode())
                        .build());
    }
}
