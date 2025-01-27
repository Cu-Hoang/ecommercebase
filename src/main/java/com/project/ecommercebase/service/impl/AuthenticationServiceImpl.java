package com.project.ecommercebase.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.ecommercebase.data.entity.RefreshToken;
import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.data.repository.RefreshTokenRepository;
import com.project.ecommercebase.data.repository.UserRepository;
import com.project.ecommercebase.dto.request.*;
import com.project.ecommercebase.enums.AccountStatus;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.service.AuthenticationService;
import com.project.ecommercebase.service.MailService;
import com.project.ecommercebase.service.RedisService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    @NonFinal
    @Value("${spring.jwt.key}")
    String key;

    @NonFinal
    @Value("${spring.jwt.access-duration}")
    String accessDuration;

    @NonFinal
    @Value("${spring.jwt.refresh-duration}")
    String refreshDuration;

    UserRepository userRepository;

    RedisService redisService;

    RefreshTokenRepository refreshTokenRepository;

    PasswordEncoder passwordEncoder;

    MailService mailService;

    @Override
    public Map<String, String> loginWithPassword(LoginRequest loginRequest, String userAgent) {
        User user = userRepository
                .findByEmailAndAccountStatus(loginRequest.email(), AccountStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        boolean authenticated = passwordEncoder.matches(loginRequest.password(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        String userId = user.getId().toString();
        String jwtID = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        Map<String, String> map = new HashMap<>();
        map.put("access token", generateAccessToken(user, userAgent, jwtID));
        map.put("refresh token", refreshToken);

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUserAgent(userAgent);
        if (refreshTokenOptional.isPresent()) {
            RefreshToken rt = refreshTokenOptional.get();
            rt.setCode(refreshToken);
            rt.setJwtID(jwtID);
            rt.setExpiredAt(LocalDateTime.now().plus(Long.parseLong(refreshDuration), ChronoUnit.SECONDS));
            refreshTokenRepository.save(rt);
        } else {
            RefreshToken rt = RefreshToken.builder()
                    .code(refreshToken)
                    .expiredAt(LocalDateTime.now().plus(Long.parseLong(refreshDuration), ChronoUnit.SECONDS))
                    .jwtID(jwtID)
                    .userAgent(userAgent)
                    .user(user)
                    .build();
            refreshTokenRepository.save(rt);
        }

        StringBuilder keyAT = this.buildAccessKey(userId, userAgent);
        redisService.setKeyWithTTL(String.valueOf(keyAT), jwtID, Integer.valueOf(accessDuration) + 5);
        return map;
    }

    @Override
    public String generateAccessToken(User user, String userAgent, String jwtID) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("ecommercebase")
                .issueTime(new Date())
                .subject(user.getId().toString())
                .expirationTime(new Date(Instant.now()
                        .plus(Long.parseLong(accessDuration), ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(jwtID)
                .claim("User-Agent", userAgent)
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().stream().map(Enum::toString).forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }

    @Override
    public Map<String, String> generateNewToken(
            HttpServletRequest httpServletRequest, RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByCode(refreshTokenRequest.refreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.UNCLASSIFIED_EXCEPTION));

        String userAgent = httpServletRequest.getHeader("User-Agent");
        if (!refreshToken.getUserAgent().equals(userAgent)) throw new AppException(ErrorCode.UNAUTHENTICATED);
        if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new AppException(ErrorCode.LOGOUT);
        }

        User user = refreshToken.getUser();
        String userId = user.getId().toString();
        String newRefreshToken = UUID.randomUUID().toString();
        String jwtID = UUID.randomUUID().toString();

        Map<String, String> map = new HashMap<>();
        map.put("access token", generateAccessToken(user, userAgent, jwtID));
        map.put("refresh token", newRefreshToken);

        StringBuilder keyAT = this.buildAccessKey(userId, userAgent);
        redisService.setKeyWithTTL(String.valueOf(keyAT), jwtID, Integer.valueOf(accessDuration) + 5);

        refreshToken.setCode(newRefreshToken);
        refreshToken.setJwtID(jwtID);
        refreshToken.setExpiredAt(LocalDateTime.now().plus(Long.parseLong(refreshDuration), ChronoUnit.SECONDS));
        refreshTokenRepository.save(refreshToken);
        return map;
    }

    @Override
    public String logout(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) throw new AppException(ErrorCode.UNAUTHENTICATED);
        String token = header.substring(7);
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String userId = signedJWT.getJWTClaimsSet().getSubject();
            String jwtID = signedJWT.getJWTClaimsSet().getJWTID();
            if (!userRepository.existsByIdAndAccountStatus(UUID.fromString(userId), AccountStatus.ACTIVE))
                throw new AppException(ErrorCode.NOT_EXISTED_USER);
            String userAgent = signedJWT.getJWTClaimsSet().getStringClaim("User-Agent");

            RefreshToken refreshToken =
                    refreshTokenRepository.findByJwtID(jwtID).orElseThrow(() -> new AppException(ErrorCode.LOGOUT));
            refreshTokenRepository.delete(refreshToken);

            StringBuilder keyAT = this.buildAccessKey(userId, userAgent);
            redisService.delete(String.valueOf(keyAT));
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return "Log out successfully";
    }

    @Override
    @Transactional
    public String logoutAllDevices(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) throw new AppException(ErrorCode.UNAUTHENTICATED);
        String token = header.substring(7);
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String userId = signedJWT.getJWTClaimsSet().getSubject();
            String jwtID = signedJWT.getJWTClaimsSet().getJWTID();
            if (!userRepository.existsByIdAndAccountStatus(UUID.fromString(userId), AccountStatus.ACTIVE))
                throw new AppException(ErrorCode.NOT_EXISTED_USER);

            if (refreshTokenRepository.existsByJwtID(jwtID))
                refreshTokenRepository.deleteAllByUserId(UUID.fromString(userId));
            else throw new AppException(ErrorCode.LOGOUT);

            StringBuilder keyAT = this.buildAccessKey(userId.toString(), "*");
            redisService.deleteByPattern(String.valueOf(keyAT));
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return "Log out all devices successfully";
    }

    @Override
    public String createEmailOtpPassword(EmailRequest emailRequest) {
        return mailService.createCode(
                emailRequest.email(), "otppassword_", 125, "OTP Password", "This code will expire within 2 minutes.");
    }

    @Override
    public Map<String, String> loginWithOTP(LoginOtpRequest loginOtpRequest, String userAgent) {
        User user = userRepository
                .findByEmailAndAccountStatus(loginOtpRequest.email(), AccountStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        String email = loginOtpRequest.email();
        Integer otp = loginOtpRequest.otp();

        StringBuilder key = new StringBuilder();
        key.append("otppassword_");
        key.append(email);

        if (redisService.getValue(String.valueOf(key)) == null
                || !redisService.getValue(String.valueOf(key)).equals(String.valueOf(otp)))
            throw new AppException(ErrorCode.INVALID_OTP);

        String userId = user.getId().toString();
        String jwtID = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        Map<String, String> map = new HashMap<>();
        map.put("access token", generateAccessToken(user, userAgent, jwtID));
        map.put("refresh token", refreshToken);

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUserAgent(userAgent);
        if (refreshTokenOptional.isPresent()) {
            RefreshToken rt = refreshTokenOptional.get();
            rt.setCode(refreshToken);
            rt.setJwtID(jwtID);
            rt.setExpiredAt(LocalDateTime.now().plus(Long.parseLong(refreshDuration), ChronoUnit.SECONDS));
            refreshTokenRepository.save(rt);
        } else {
            RefreshToken rt = RefreshToken.builder()
                    .code(refreshToken)
                    .expiredAt(LocalDateTime.now().plus(Long.parseLong(refreshDuration), ChronoUnit.SECONDS))
                    .jwtID(jwtID)
                    .userAgent(userAgent)
                    .user(user)
                    .build();
            refreshTokenRepository.save(rt);
        }

        StringBuilder keyAT = this.buildAccessKey(userId, userAgent);
        redisService.setKeyWithTTL(String.valueOf(keyAT), jwtID, Integer.valueOf(accessDuration) + 5);
        return map;
    }

    @Override
    public String createEmailOtpResetPassword(EmailRequest emailRequest) {
        return mailService.createCode(
                emailRequest.email(),
                "otpresetpassword_",
                305,
                "OTP Reset Password",
                "This code will expire within 5 minutes.");
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository
                .findByEmailAndAccountStatus(resetPasswordRequest.email(), AccountStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        String email = resetPasswordRequest.email();
        Integer otp = resetPasswordRequest.otp();

        StringBuilder key = new StringBuilder();
        key.append("otpresetpassword_");
        key.append(email);

        if (redisService.getValue(String.valueOf(key)) == null
                || !redisService.getValue(String.valueOf(key)).equals(String.valueOf(otp)))
            throw new AppException(ErrorCode.INVALID_OTP);

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepository.save(user);
        return "Update password successfully";
    }

    public static StringBuilder buildAccessKey(String userId, String userAgent) {
        StringBuilder keyAT = new StringBuilder();
        keyAT.append(userId);
        keyAT.append(":");
        keyAT.append(userAgent);
        return keyAT;
    }
}
