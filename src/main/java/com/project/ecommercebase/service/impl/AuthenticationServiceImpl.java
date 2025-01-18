package com.project.ecommercebase.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.project.ecommercebase.data.entity.RefreshToken;
import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.data.repository.RefreshTokenRepository;
import com.project.ecommercebase.data.repository.UserRepository;
import com.project.ecommercebase.dto.request.LoginRequest;
import com.project.ecommercebase.dto.request.RefreshTokenRequest;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.service.AuthenticationService;
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

    @Override
    public Map<String, String> login(LoginRequest loginRequest, String userAgent) {
        User user = userRepository
                .findByEmail(loginRequest.email())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(loginRequest.password(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        StringBuilder keyAT = new StringBuilder();
        String userId = user.getId().toString();
        String jwtID = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();
        Map<String, String> map = new HashMap<>();
        keyAT.append(userId);
        keyAT.append(":");
        keyAT.append(userAgent);
        map.put("access token", generateAccessToken(user, userAgent, jwtID));
        map.put("refresh token", refreshToken);

        redisService.setKeyWithTTL(String.valueOf(keyAT), jwtID, Integer.valueOf(accessDuration) + 5);

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
        return map;
    }

    @Override
    public String generateAccessToken(User user, String userAgent, String jwtID) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("ecommercebase")
                .issueTime(new Date())
                .subject(user.getEmail())
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
        if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) throw new AppException(ErrorCode.LOGOUT);

        StringBuilder keyAT = new StringBuilder();
        String userId = refreshToken.getUser().getId().toString();
        User user = refreshToken.getUser();
        String newRefreshToken = UUID.randomUUID().toString();
        String jwtID = UUID.randomUUID().toString();
        Map<String, String> map = new HashMap<>();
        keyAT.append(userId);
        keyAT.append(":");
        keyAT.append(userAgent);
        map.put("access token", generateAccessToken(user, userAgent, jwtID));
        map.put("refresh token", newRefreshToken);

        redisService.setKeyWithTTL(String.valueOf(keyAT), jwtID, Integer.valueOf(accessDuration) + 5);

        refreshToken.setCode(newRefreshToken);
        refreshToken.setJwtID(jwtID);
        refreshToken.setExpiredAt(LocalDateTime.now().plus(Long.parseLong(refreshDuration), ChronoUnit.SECONDS));
        refreshTokenRepository.save(refreshToken);

        return map;
    }

    @Override
    public Boolean verifyRefreshToken(LoginRequest loginRequest, String token) {
        return null;
    }

    @Override
    public void refreshAccessToken(LoginRequest loginRequest, String token) {}

    @Override
    public void logout(LoginRequest loginRequest) {}

    @Override
    public void logoutAll(LoginRequest loginRequest) {}
}
