package com.project.ecommercebase.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.data.repository.UserRepository;
import com.project.ecommercebase.enums.Role;
import com.project.ecommercebase.enums.Status;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                Set<Role> roles = new HashSet<>(List.of(Role.ADMIN, Role.VENDOR, Role.CUSTOMER));
                User user = User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin123"))
                        .status(Status.ACTIVE)
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("Admin user has been created with default password, please change it");
            }
        };
    }
}
