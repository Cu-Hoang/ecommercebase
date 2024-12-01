package com.project.ecommercebase.controller;

import org.springframework.web.bind.annotation.*;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.UserRequest;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.dto.response.UserResponse;
import com.project.ecommercebase.service.MailService;
import com.project.ecommercebase.service.impl.UserServiceImpl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(Endpoint.ENDPOINT_USER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserServiceImpl userService;
    MailService mailService;

    @PostMapping(value = Endpoint.UserEndpoint.CREATE_USER)
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.createCustomer(userRequest))
                .message("Created user successfully")
                .build();
    }

    @PostMapping(value = "/sendmail")
    public ApiResponse<String> sendMail() {
        mailService.sendSimpleMessage("cungochoang2002@gmail.com", "Test", "This is a test");
        return ApiResponse.<String>builder().message("Mail sent").build();
    }
}
