package com.project.ecommercebase.service.impl;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.data.repository.UserRepository;
import com.project.ecommercebase.dto.request.UserRequest;
import com.project.ecommercebase.dto.request.UserUpdateRequest;
import com.project.ecommercebase.dto.response.UserResponse;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.mapper.UserMapper;
import com.project.ecommercebase.service.BaseService;
import com.project.ecommercebase.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, BaseService<User, UUID, UserRepository> {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @Override
    public UserRepository getRepository() {
        return this.userRepository;
    }

    @Override
    public UserResponse createCustomer(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        if (userRepository.existsByEmail(user.getEmail())) throw new AppException(ErrorCode.USER_EXISTED);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of("USER"));
        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateToVendor() {
        return null;
    }

    @Override
    public UserResponse createVendor(UserRequest userRequest) {
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return null;
    }

    @Override
    public UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest) {
        return null;
    }
}
