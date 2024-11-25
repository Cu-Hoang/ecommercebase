package com.project.ecommercebase.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommercebase.constant.Endpoint;
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

    @GetMapping(value = Endpoint.UserEndpoint.CREATE_USER)
    public String createUser() {
        return "User created";
    }
}
