package com.example.mbaningapijpapractice.features.auth;


import com.example.mbaningapijpapractice.features.auth.dto.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

public interface AuthService {

    void register(RegisterRequest registerRequest) throws MessagingException;

    void verify(@Valid VerifyRequest verifyRequest);

    JwtResponse login(@Valid LoginRequest loginRequest);

    JwtResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest);
}
