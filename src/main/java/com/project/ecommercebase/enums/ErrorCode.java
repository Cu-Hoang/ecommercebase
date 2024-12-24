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
    INVALID_FIELD(1002, "Invalid field.", HttpStatus.BAD_REQUEST),
    NOT_SENDING_CODE(1003, "Cannot send code, please try again.", HttpStatus.BAD_REQUEST),
    NOT_VERIFIED_EMAIL(1004, "Email has not been verified yet.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission.", HttpStatus.FORBIDDEN),
    UNCLASSIFIED_EXCEPTION(9999, "Unclassified exception", HttpStatus.INTERNAL_SERVER_ERROR);
    int code;
    String message;
    HttpStatus httpStatus;

    public static boolean exists(String value) {
        for (ErrorCode code : ErrorCode.values()) if (code.name().equals(value)) return true;
        return false;
    }
}
