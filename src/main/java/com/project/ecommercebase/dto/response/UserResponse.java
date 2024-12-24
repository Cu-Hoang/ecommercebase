package com.project.ecommercebase.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    UUID id;

    String firstname;

    String lastname;

    String username;

    String email;

    LocalDate dateOfBirth;

    String avatar;
}
