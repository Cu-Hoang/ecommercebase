package com.project.ecommercebase.data.repository;

import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByUserAgent(String userAgent);

    Optional<RefreshToken> findByCode(String code);

    Optional<RefreshToken> findByJwtID(String jwiId);

    Boolean existsByJwtID(String jwtID);

    @Transactional
    void deleteAllByUserId(UUID userId);
}
