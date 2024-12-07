package com.project.ecommercebase.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.UserRequest;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.dto.response.UserResponse;
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

    KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping(value = Endpoint.UserEndpoint.CREATE_USER)
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.createCustomer(userRequest))
                .message("Created user successfully")
                .build();
    }

    @PostMapping(value = "/sendmail")
    public ApiResponse<String> sendMail(@RequestBody String email) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("mail_topic", email);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + email + "] with offset=["
                        + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + email + "] due to : " + ex.getMessage());
            }
        });
        return ApiResponse.<String>builder()
                .message("Mail is sent successfully.")
                .build();
    }
}
