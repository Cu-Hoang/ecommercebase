package com.project.ecommercebase.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    UUID id;

    String username;

    String email;

    LocalDate dateOfBirth;

    String avatar;
}
