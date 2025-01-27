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
    EXISTED_USER(1001, "User existed", HttpStatus.BAD_REQUEST),
    NOT_EXISTED_USER(1002, "User did not exist", HttpStatus.BAD_REQUEST),
    INVALID_FIELD(1003, "Invalid field.", HttpStatus.BAD_REQUEST),
    CANNOT_SENDING_CODE(1004, "Cannot send code, please try again.", HttpStatus.BAD_REQUEST),
    NOT_VERIFIED_EMAIL(1005, "Email has not been verified yet.", HttpStatus.BAD_REQUEST),
    BE_VENDOR(1006, "You have already been a vendor.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission.", HttpStatus.FORBIDDEN),
    LOGOUT(1009, "You logged out, please log in.", HttpStatus.BAD_REQUEST),
    INVALID_OTP(1010, "Otp is invalid", HttpStatus.BAD_REQUEST),
    UNCLASSIFIED_EXCEPTION(9999, "Unclassified exception", HttpStatus.INTERNAL_SERVER_ERROR);
    int code;
    String message;
    HttpStatus httpStatus;

    public static boolean exists(String value) {
        for (ErrorCode code : ErrorCode.values()) if (code.name().equals(value)) return true;
        return false;
    }
}
