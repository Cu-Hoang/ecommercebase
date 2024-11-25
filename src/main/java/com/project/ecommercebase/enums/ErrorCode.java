package com.project.ecommercebase.enums;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    INVALID_FIELD(1006, "Invalid field.", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1006, "Invalid key.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission.", HttpStatus.FORBIDDEN),
    UNCLASSIFIED_EXCEPTION(9999, "Unclassified exception", HttpStatus.INTERNAL_SERVER_ERROR);
    int code;
    String message;
    HttpStatus httpStatus;
}
