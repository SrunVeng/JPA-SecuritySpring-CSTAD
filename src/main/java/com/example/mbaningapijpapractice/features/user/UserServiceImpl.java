package com.example.mbaningapijpapractice.features.user;


import com.example.mbaningapijpapractice.domain.User;
import com.example.mbaningapijpapractice.features.user.dto.Request.CreateUserRequest;
import com.example.mbaningapijpapractice.features.user.dto.UserResponse;
import com.example.mbaningapijpapractice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

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

        User user = userMapper.fromUserRequest(createUserRequest);
        user.setCreatedAt(LocalDate.now());
        user.setIsBlock(false);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsVerified(createUserRequest.profileImage() != null && !createUserRequest.profileImage().trim().isEmpty());
        user.setIsAccountNonExpired(false);
        user.setIsCredentialsNonExpired(false);
        user.setIsAccountNonLocked(false);
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }
}
