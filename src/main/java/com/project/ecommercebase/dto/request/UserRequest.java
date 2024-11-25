package com.project.ecommercebase.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String username;

    String email;

    String password;

    LocalDateTime lastLoginDate;

    LocalDate dateOfBirth;
}
