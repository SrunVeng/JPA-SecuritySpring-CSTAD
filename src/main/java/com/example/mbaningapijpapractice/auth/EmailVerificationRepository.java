package com.example.mbaningapijpapractice.auth;

import com.example.mbaningapijpapractice.domain.EmailVerification;
import com.example.mbaningapijpapractice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findByUser(User user);
}
