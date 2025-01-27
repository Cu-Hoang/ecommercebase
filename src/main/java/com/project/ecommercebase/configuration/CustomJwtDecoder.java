package com.project.ecommercebase.configuration;

import java.text.ParseException;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.service.RedisService;
import com.project.ecommercebase.service.impl.AuthenticationServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${spring.jwt.key}")
    private String key;

    private final HttpServletRequest httpServletRequest;

    private final RedisService redisService;

    public CustomJwtDecoder(HttpServletRequest httpServletRequest, RedisService redisService) {
        this.httpServletRequest = httpServletRequest;
        this.redisService = redisService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            JWSVerifier jwsVerifier = new MACVerifier(key.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            String userAgent = signedJWT.getJWTClaimsSet().getStringClaim("User-Agent");
            String userId = signedJWT.getJWTClaimsSet().getSubject();
            StringBuilder keyAT = AuthenticationServiceImpl.buildAccessKey(userId, userAgent);
            boolean verified = signedJWT.verify(jwsVerifier);
            if (!verified
                    || !expirationTime.after(new Date())
                    || !httpServletRequest.getHeader("User-Agent").equals(userAgent)
                    || redisService.getValue(String.valueOf(keyAT)) == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
        } catch (JOSEException | ParseException e) {
            log.error(e.getMessage());
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.key.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build()
                .decode(token);
    }
}
