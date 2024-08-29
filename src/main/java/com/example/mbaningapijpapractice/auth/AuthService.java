package com.example.mbaningapijpapractice.auth;

import com.example.mbaningapijpapractice.auth.dto.JwtResponse;
import com.example.mbaningapijpapractice.auth.dto.LoginRequest;
import com.example.mbaningapijpapractice.auth.dto.RegisterRequest;
import com.example.mbaningapijpapractice.auth.dto.VerifyRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

public interface AuthService {

    void register(RegisterRequest registerRequest) throws MessagingException;

    void verify(@Valid VerifyRequest verifyRequest);

    JwtResponse login(@Valid LoginRequest loginRequest);
}
