package com.example.mbaningapijpapractice.features.user.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record VerifyResetPasswordRequest(

        @NotBlank
        String email,
        @NotBlank
        String verificationCode,
        @NotBlank
        String newPassword

) {
}
