package com.example.mbaningapijpapractice.features.user.dto;

public record UserResponse(
        String name,
        String gender,
        Boolean isVerified
) {
}
