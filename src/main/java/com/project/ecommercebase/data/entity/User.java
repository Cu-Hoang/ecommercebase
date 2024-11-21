package com.project.ecommercebase.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.project.ecommercebase.enums.AccountStatus;
import jakarta.persistence.*;

import org.hibernate.annotations.UuidGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class User extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    @ToString.Exclude
    String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus = AccountStatus.PENDING;

    LocalDateTime lastLoginDate;

    LocalDate dateOfBirth;

    LocalDateTime lockedUntil;

    Integer failedLoginAttempts;

    String avatar;

    Set<String> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = User.class)
    Set<Code> codes;
}
