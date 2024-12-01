package com.project.ecommercebase.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String firstname;

    String lastname;

    String username;

    @Email(message = "Invalid email")
    String email;

    @Size(min = 8, message = "Password must be at least 8 characters long.")
    String password;

    LocalDate dateOfBirth;
}
