package com.project.ecommercebase.service.impl;

import java.util.*;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.data.entity.Shop;
import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.data.repository.ShopRepository;
import com.project.ecommercebase.data.repository.UserRepository;
import com.project.ecommercebase.dto.request.*;
import com.project.ecommercebase.dto.response.UserResponse;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.enums.Role;
import com.project.ecommercebase.enums.Status;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.mapper.UserMapper;
import com.project.ecommercebase.service.MailService;
import com.project.ecommercebase.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    ShopRepository shopRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    RedisServiceImpl redisService;

    MailService mailService;

    @Override
    public String createEmailVerificationCode(EmailRequest emailRequest) {
        return mailService.createCode(
                emailRequest.email(),
                "verifying_",
                305,
                "Email Verification",
                "This code will expire within 5 minutes.");
    }

    @Override
    public String verifyEmail(EmailVerificationRequest emailVerificationRequest) {
        String email = emailVerificationRequest.email();
        Integer verificationCode = emailVerificationRequest.verificationCode();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getStatus() == Status.PENDING) return "Email already verifies.";
            else if (user.get().getStatus() == Status.ACTIVE) return "Email already registers.";
        }

        StringBuilder key = new StringBuilder();
        key.append("verifying_");
        key.append(email);

        if (redisService.getValue(String.valueOf(key)) != null
                && redisService.getValue(String.valueOf(key)).equals(String.valueOf(verificationCode))) {
            User newUser = new User();
            newUser.setEmail(email);
            userRepository.save(newUser);
            redisService.delete(String.valueOf(key));
            return "Verify email successfully.";
        }
        return "Verify email failed.";
    }

    @Override
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest, Role role) {
        User user = userRepository
                .findByEmail(userRegisterRequest.email())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_VERIFIED_EMAIL));
        if (user.getStatus() != Status.PENDING) throw new AppException(ErrorCode.UNCLASSIFIED_EXCEPTION);

        userMapper.registerUser(user, userRegisterRequest);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.password()));
        user.setStatus(Status.ACTIVE);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @Override
    public String updateToVendor(EmailRequest emailRequest) {
        User user = userRepository
                .findByEmail(emailRequest.email())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        if (user.getStatus() != Status.ACTIVE) throw new AppException(ErrorCode.NOT_EXISTED_USER);
        if (user.getRoles().contains(Role.VENDOR)) throw new AppException(ErrorCode.BE_VENDOR);

        Set<Role> roles = user.getRoles();
        roles.add(Role.VENDOR);
        user.setRoles(roles);
        userMapper.userToUserResponse(userRepository.save(user));
        return "The customer has just been promoted to a vendor successfully.";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String updateShop(UUID id, UpdateShopRequest updateShopRequest) {
        User user = userRepository
                .findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Optional<Shop> shopOptional = shopRepository.findByUser(user);
        if (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();
            if (shop.getStatus() == Status.ACTIVE) throw new AppException(ErrorCode.REGISTERED_SHOP);
            else throw new AppException(ErrorCode.UNCLASSIFIED_EXCEPTION);
        }
        Shop shop = Shop.builder()
                .name(updateShopRequest.name())
                .province(updateShopRequest.province())
                .user(user)
                .build();
        user.setShop(shop);
        userRepository.save(user);
        return "Updated shop successfully";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponse)
                .toList();
    }

    @Override
    @PreAuthorize("T(java.util.UUID).fromString(authentication.name) == #id")
    public UserResponse getUserById(UUID id) {
        User user = userRepository
                .findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        return userMapper.userToUserResponse(user);
    }

    @Override
    @PreAuthorize("T(java.util.UUID).fromString(authentication.name) == #id")
    public UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository
                .findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        userMapper.updaterUser(user, userUpdateRequest);
        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("T(java.util.UUID).fromString(authentication.name) == #id")
    public String updatePassword(UUID id, UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository
                .findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        boolean authenticated = passwordEncoder.matches(updatePasswordRequest.oldPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
        userRepository.save(user);
        return "Update password successfully";
    }
}
