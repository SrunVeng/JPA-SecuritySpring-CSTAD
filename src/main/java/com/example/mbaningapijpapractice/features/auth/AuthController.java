package com.example.mbaningapijpapractice.features.auth;


import com.example.mbaningapijpapractice.features.auth.dto.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    void Register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        authService.register(registerRequest);
    }


    @PostMapping("/verify")
    void verify(@Valid @RequestBody VerifyRequest verifyRequest) {
        authService.verify(verifyRequest);
    }

    @PostMapping("/login")
    JwtResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);

    }

    @PostMapping("/refreshToken")
    JwtResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest ) {
        return authService.refreshToken(refreshTokenRequest);
    }

}
