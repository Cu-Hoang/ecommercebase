package com.project.ecommercebase.service.impl;

import java.security.SecureRandom;
import java.util.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.data.repository.UserRepository;
import com.project.ecommercebase.dto.request.EmailRequest;
import com.project.ecommercebase.dto.request.EmailVerificationRequest;
import com.project.ecommercebase.dto.request.UserRegisterRequest;
import com.project.ecommercebase.dto.request.UserUpdateRequest;
import com.project.ecommercebase.dto.response.UserResponse;
import com.project.ecommercebase.enums.AccountStatus;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.enums.Role;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.mapper.UserMapper;
import com.project.ecommercebase.service.BaseService;
import com.project.ecommercebase.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, BaseService<User, UUID, UserRepository> {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    RedisServiceImpl redisService;

    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public UserRepository getRepository() {
        return this.userRepository;
    }

    @Override
    public String createEmailVerificationCode(EmailRequest emailRequest) {
        SecureRandom secureRandom = new SecureRandom();
        int verificationCode = 100000 + secureRandom.nextInt(900000);

        String email = emailRequest.email();
        StringBuilder key = new StringBuilder();
        key.append("verifying_");
        key.append(email);
        redisService.setKeyWithTTL(String.valueOf(key), String.valueOf(verificationCode), 70);

        try {
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send("mail_topic", new EmailVerificationRequest(email, verificationCode));
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Sent message=[" + email + "] with offset=["
                            + result.getRecordMetadata().offset() + "]");
                } else {
                    log.error("Unable to send message=[" + email + "] due to : " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.CANNOT_SENDING_CODE);
        }
        return "Sent code successfully";
    }

    @Override
    public String verifyEmail(EmailVerificationRequest emailVerificationRequest) {
        String email = emailVerificationRequest.email();
        Integer verificationCode = emailVerificationRequest.verificationCode();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getAccountStatus() == AccountStatus.PENDING) return "Email already verifies";
            else if (user.get().getAccountStatus() == AccountStatus.ACTIVE) return "Email already registers";
        }

        StringBuilder key = new StringBuilder();
        key.append("verifying_");
        key.append(email);

        if (redisService.getValue(String.valueOf(key)) != null
                && redisService.getValue(String.valueOf(key)).equals(String.valueOf(verificationCode))) {
            User newUser = new User();
            newUser.setEmail(email);
            userRepository.save(newUser);
            return "Verify email successfully";
        }
        return "Verify email failed";
    }

    @Override
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest, Role role) {
        User user = userRepository
                .findByEmail(userRegisterRequest.email())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_VERIFIED_EMAIL));
        if (user.getAccountStatus() != AccountStatus.PENDING) throw new AppException(ErrorCode.UNCLASSIFIED_EXCEPTION);

        userMapper.registerUser(user, userRegisterRequest);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.password()));
        user.setAccountStatus(AccountStatus.ACTIVE);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateToVendor(EmailRequest emailRequest) {
        User user = userRepository
                .findByEmail(emailRequest.email())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        if (user.getAccountStatus() != AccountStatus.ACTIVE) throw new AppException(ErrorCode.NOT_EXISTED_USER);
        if (user.getRoles().contains(Role.VENDOR)) throw new AppException(ErrorCode.BE_VENDOR);

        Set<Role> roles = user.getRoles();
        roles.add(Role.VENDOR);
        user.setRoles(roles);

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::userToUserResponse).toList();
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository
                .findByIdAndAccountStatus(id, AccountStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository
                .findByIdAndAccountStatus(id, AccountStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        userMapper.updaterUser(user, userUpdateRequest);
        return userMapper.userToUserResponse(userRepository.save(user));
    }
}
