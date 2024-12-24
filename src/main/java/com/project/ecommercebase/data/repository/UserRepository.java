package com.project.ecommercebase.data.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
