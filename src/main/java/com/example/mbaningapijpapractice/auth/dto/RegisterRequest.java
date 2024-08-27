package com.example.mbaningapijpapractice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String name,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotBlank(message = "National ID is required")
        String nationalCardId,
        @NotBlank(message = "Gender is required")
        String gender,
        @NotBlank(message = "Pin is required")
        String pin,
        @NotBlank(message = "password is required")
        String password,
        @NotBlank(message = "confirmPassword is required")
        String confirmPassword
) {
}
