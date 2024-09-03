package com.example.mbaningapijpapractice.features.auth;

import com.example.mbaningapijpapractice.Util.Utility;
import com.example.mbaningapijpapractice.domain.EmailVerification;
import com.example.mbaningapijpapractice.domain.Role;
import com.example.mbaningapijpapractice.domain.User;
import com.example.mbaningapijpapractice.features.auth.dto.*;
import com.example.mbaningapijpapractice.features.user.UserRepository;
import com.example.mbaningapijpapractice.mapper.UserMapper;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    private final JwtEncoder jwtEncoderAccessToken;
    private final JwtEncoder jwtEncoderRefreshToken;


    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

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
                        <p>If all the above information is correct,
                        please use the following verification code to verify your account:</p>
                        <p>note that the code will be expired in 5 minutes</p>
                        <h2>%s</h2>
                        """, registerRequest.name(),
                registerRequest.phoneNumber(),
                registerRequest.email(),
                registerRequest.nationalCardId(),
                registerRequest.gender(),
                emailVerification.getVerificationCode());

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
                emailVerificationRepository.findByUser(user).orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

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
        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequest.email()
                , loginRequest.password());
        auth = daoAuthenticationProvider.authenticate(auth);

        log.info("AuthorName: {}",auth.getAuthorities().toString());

        String scope = auth.
                getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // Generate JWT token to response

        //1. Define JwtClaimSet  (Payload)
        Instant now = Instant.now();
        JwtClaimsSet accessJwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Access Token")
                .issuer(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .audience(List.of("REACT JS"))
                .claim("scope", scope)
                .build();

        JwtClaimsSet refreshJwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Refresh Token")
                .issuer(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.DAYS))
                .audience(List.of("REACT JS"))
                .claim("scope", scope)
                .build();

        //2. Generate Token
        String accessToken = jwtEncoderAccessToken.encode(JwtEncoderParameters.from(accessJwtClaimsSet)).getTokenValue();
        String refreshToken = jwtEncoderRefreshToken.encode(JwtEncoderParameters.from(refreshJwtClaimsSet)).getTokenValue();


        return JwtResponse.builder()
                .accessToken(accessToken)
                .tokenType("AccessToken")
                .refreshToken(refreshToken)
                .build();

    }
    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenRequest.refreshToken());

        auth = jwtAuthenticationProvider.authenticate(auth);

        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        log.info("New Scope: {}", scope);
        log.info("Auth: {}", auth);

        Instant now = Instant.now();

        Jwt jwt = (Jwt) auth.getPrincipal();

        // Create access token claims set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs", "reactjs"))
                .subject("Access Token")
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt encodedJwt = jwtEncoderAccessToken.encode(jwtEncoderParameters);

        String accessToken = encodedJwt.getTokenValue();
        String refreshToken = refreshTokenRequest.refreshToken();

        if (Duration.between(Instant.now(), jwt.getExpiresAt()).toDays() < 2) {
            // Create refresh token claims set
            JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                    .id(auth.getName())
                    .issuedAt(now)
                    .issuer("web")
                    .audience(List.of("nextjs", "reactjs"))
                    .subject("Refresh Token")
                    .expiresAt(now.plus(7, ChronoUnit.DAYS))
                    .build();
            JwtEncoderParameters jwtEncoderParametersRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
            Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);
            refreshToken = jwtRefreshToken.getTokenValue();
        }

        return JwtResponse.builder()
                .tokenType("Refresh Token")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
