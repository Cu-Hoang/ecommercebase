package com.project.ecommercebase.service;

import java.util.List;
import java.util.UUID;

import com.project.ecommercebase.dto.request.UserRequest;
import com.project.ecommercebase.dto.request.UserUpdateRequest;
import com.project.ecommercebase.dto.response.UserResponse;

public interface UserService {
    UserResponse createCustomer(UserRequest userRequest);

    UserResponse updateToVendor();

    UserResponse createVendor(UserRequest userRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(UUID id);

    UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest);
}
