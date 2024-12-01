package com.project.ecommercebase.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {}
