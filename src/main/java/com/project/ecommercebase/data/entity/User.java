package com.project.ecommercebase.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.UuidGenerator;

import com.project.ecommercebase.enums.Role;
import com.project.ecommercebase.enums.Status;

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

    String firstname;

    String lastname;

    @Column(unique = true)
    String username;

    @Column(nullable = false, unique = true)
    String email;

    @ToString.Exclude
    String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    Status status = Status.PENDING;

    LocalDate dateOfBirth;

    LocalDateTime lockedUntil;

    Integer failedLoginAttempts;

    String avatar;

    @Enumerated(EnumType.STRING)
    Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = Shop.class)
    @JoinTable(
            name = "user_shop",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "shop_id", referencedColumnName = "id")})
    Shop shop;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = RefreshToken.class)
    Set<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Address.class)
    Set<Address> addresses;
}
