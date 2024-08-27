package com.example.mbaningapijpapractice.auth;


import com.example.mbaningapijpapractice.auth.dto.RegisterRequest;
import com.example.mbaningapijpapractice.auth.dto.VerifyRequest;
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
    void verify(@Valid @RequestBody VerifyRequest verifyRequest) throws MessagingException {
        authService.verify(verifyRequest);
    }


}
