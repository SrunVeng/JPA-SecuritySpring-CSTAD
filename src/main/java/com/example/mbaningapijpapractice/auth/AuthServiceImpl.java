package com.example.mbaningapijpapractice.auth;

import com.example.mbaningapijpapractice.Util.Utility;
import com.example.mbaningapijpapractice.auth.dto.JwtResponse;
import com.example.mbaningapijpapractice.auth.dto.LoginRequest;
import com.example.mbaningapijpapractice.auth.dto.RegisterRequest;
import com.example.mbaningapijpapractice.auth.dto.VerifyRequest;
import com.example.mbaningapijpapractice.domain.EmailVerification;
import com.example.mbaningapijpapractice.domain.Role;
import com.example.mbaningapijpapractice.domain.User;
import com.example.mbaningapijpapractice.features.user.UserRepository;
import com.example.mbaningapijpapractice.mapper.UserMapper;
import com.example.mbaningapijpapractice.security.JwtConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;
    private final JwtEncoder jwtEncoderAccessToken;

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtConfig jwtConfig;

    @Value("${spring.mail.username}")
    private String adminMail;

    @Override
    public void register(RegisterRequest registerRequest) throws MessagingException {

        // Validate NID
        if (userRepository.existsBynationalCardId(registerRequest.nationalCardId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "NID already exists");
        }
        // Validate Phone

        if (userRepository.existsByPhoneNumber(registerRequest.phoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }
        // Validate confirmPassword

        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match");
        }

        User user = userMapper.fromUserRequest(registerRequest);
        user.setCreatedAt(LocalDate.now());
        user.setIsBlock(false);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsAccountNonExpired(false);
        user.setIsCredentialsNonExpired(false);
        user.setIsDeleted(true);
        user.setIsVerified(false);
        user.setNationalCardId(registerRequest.nationalCardId());
        user.setIsAccountNonLocked(false);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(4).orElseThrow());
        user.setRoles(roles);
        User user1 = userRepository.save(user);

        //1. Prepare Email verification data
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setVerificationCode(Utility.generateSecureCode());
        emailVerification.setUser(user1);
        emailVerification.setExpiryTime(LocalTime.now().plusMinutes(3));
        emailVerificationRepository.save(emailVerification);

        //2. Prepare Send email

        String html = String.format(Locale.ENGLISH, """
                <h1>MBanking - Email Verification</h1>
                <hr/>
                <p><strong>Name:</strong> %s</p>
                <p><strong>Phone Number:</strong> %s</p>
                <p><strong>Email:</strong> %s</p>
                <p><strong>National ID Card:</strong> %s</p>
                <p><strong>Gender:</strong> %s</p>
                <hr/>
                <p>If all the above information is correct, please use the following verification code to verify your account:</p>
                <p>note that the code will be expired in 5 minutes</p>
                <h2>%s</h2>
                """, registerRequest.name(), registerRequest.phoneNumber(), registerRequest.email(), registerRequest.nationalCardId(), registerRequest.gender(), emailVerification.getVerificationCode());

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Email Verification - MBanking");
        helper.setTo(user1.getEmail());
        helper.setFrom(adminMail);
        helper.setText(html, true);

        mailSender.send(mimeMessage);
    }

    @Override
    public void verify(VerifyRequest verifyRequest) {

        User user = userRepository.findByemail(verifyRequest.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        EmailVerification emailVerification =
                emailVerificationRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (!verifyRequest.verificationCode().equals(emailVerification.getVerificationCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Verification code does not match");
        }

        if (emailVerification.getExpiryTime().isBefore(LocalTime.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code is expired");
        }
        user.setIsVerified(true);
        user.setIsDeleted(false);
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        //1. Authenticate
        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        daoAuthenticationProvider.authenticate(auth);

        // Generate JWT token to response

        //1. Define JwtClaimSet  (Payload)
        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Access Token")
                .issuer(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .audience(List.of("REACT JS"))
                .build();

        //2. Generate Token
        String AccessToken = jwtEncoderAccessToken.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        return JwtResponse.builder()
                .accessToken(AccessToken)
                .tokenType("AccessToken")
                .build();

    }
}
