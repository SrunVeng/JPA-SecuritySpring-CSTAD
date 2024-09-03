package com.example.mbaningapijpapractice.features.user.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(

        @NotBlank
        String email,
        @NotBlank
        String name

) {
}
