package com.project.ecommercebase.data.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.enums.Status;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, Status status);

    Boolean existsByIdAndStatus(UUID id, Status status);

    Optional<User> findByIdAndStatus(UUID id, Status status);
}
