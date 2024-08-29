package com.example.mbaningapijpapractice.mapper;


import com.example.mbaningapijpapractice.auth.dto.RegisterRequest;
import com.example.mbaningapijpapractice.domain.User;
import com.example.mbaningapijpapractice.features.user.dto.Request.CreateUserRequest;
import com.example.mbaningapijpapractice.features.user.dto.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserResponse> toUserResponseList(List<User> user);

    UserResponse toUserResponse(User user);

    User fromUserCreate(CreateUserRequest createUserRequest);

    User fromUserRequest(RegisterRequest registerRequest);
}
