package com.example.mbaningapijpapractice.features.user;


import com.example.mbaningapijpapractice.Util.Utility;
import com.example.mbaningapijpapractice.features.auth.EmailVerificationRepository;
import com.example.mbaningapijpapractice.features.auth.RoleRepository;
import com.example.mbaningapijpapractice.domain.EmailVerification;
import com.example.mbaningapijpapractice.domain.Role;
import com.example.mbaningapijpapractice.domain.User;
import com.example.mbaningapijpapractice.features.user.dto.Request.CreateUserRequest;
import com.example.mbaningapijpapractice.features.user.dto.Request.ResetPasswordRequest;
import com.example.mbaningapijpapractice.features.user.dto.Request.VerifyResetPasswordRequest;
import com.example.mbaningapijpapractice.features.user.dto.UserResponse;
import com.example.mbaningapijpapractice.mapper.UserMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String adminMail;

    @Override
    public List<UserResponse> findAllUser() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserResponseList(users);

    }

    @Override
    public UserResponse findUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID not exist"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findUserByUuid(String uuid) {

        User user = userRepository.findByuuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Uuid not exist"));
        return userMapper.toUserResponse(user);

    }

    @Override
    public UserResponse findUserByPhoneNumber(String PhoneNumber) {
        User user = userRepository.findByPhoneNumber(PhoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone number not exist"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse createNewUser(CreateUserRequest createUserRequest) {

        //Validate NID
        if (userRepository.existsBynationalCardId(createUserRequest.nationalCardId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "NID already exists");
        }
        // Validate Phone

        if (userRepository.existsByPhoneNumber(createUserRequest.phoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }
        // Validate confirmPassword

        if (!createUserRequest.password().equals(createUserRequest.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match");
        }

        User user = userMapper.fromUserCreate(createUserRequest);
        user.setCreatedAt(LocalDate.now());
        user.setIsBlock(false);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsAccountNonExpired(false);
        user.setIsCredentialsNonExpired(false);
        user.setIsVerified(false);
        user.setIsDeleted(true);
        user.setIsAccountNonLocked(false);
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        User savedUser = userRepository.save(user);
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(4).orElseThrow());
        user.setRoles(roles);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws MessagingException {

        User user = userRepository.findByemail(resetPasswordRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not exist"));

        if (!user.getName().equals(resetPasswordRequest.name())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name not found");
        }

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setVerificationCode(Utility.generateSecureCode());
        emailVerification.setExpiryTime(LocalTime.now().plusMinutes(5));
        emailVerification.setUser(user);

        emailVerificationRepository.save(emailVerification);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(user.getEmail());
        helper.setFrom(adminMail);
        helper.setText(emailVerification.getVerificationCode());
        mailSender.send(mimeMessage);


    }

    @Override
    public void verifyResetPassword(VerifyResetPasswordRequest verifyResetPasswordRequest) {
        User user = userRepository.findByemail(verifyResetPasswordRequest.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        EmailVerification emailVerification =
                emailVerificationRepository.findByUser(user).orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (!verifyResetPasswordRequest.verificationCode().equals(emailVerification.getVerificationCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Verification code does not match");
        }

        if (emailVerification.getExpiryTime().isBefore(LocalTime.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code is expired");
        }
        
        user.setPassword(passwordEncoder.encode(verifyResetPasswordRequest.newPassword()));
        userRepository.save(user);

    }
}
