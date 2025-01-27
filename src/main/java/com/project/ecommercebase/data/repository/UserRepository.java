package com.project.ecommercebase.data.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.enums.AccountStatus;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndAccountStatus(String email, AccountStatus accountStatus);

    Boolean existsByIdAndAccountStatus(UUID id, AccountStatus accountStatus);

    Optional<User> findByIdAndAccountStatus(UUID id, AccountStatus accountStatus);
}
