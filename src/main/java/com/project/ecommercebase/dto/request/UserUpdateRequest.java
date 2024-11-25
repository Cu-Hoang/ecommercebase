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
public class UserUpdateRequest {
    String username;

    String email;

    LocalDateTime lastLoginDate;

    LocalDate dateOfBirth;
}
