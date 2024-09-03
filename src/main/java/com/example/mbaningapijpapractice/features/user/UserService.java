package com.example.mbaningapijpapractice.features.user;

import com.example.mbaningapijpapractice.features.user.dto.Request.CreateUserRequest;
import com.example.mbaningapijpapractice.features.user.dto.Request.ResetPasswordRequest;
import com.example.mbaningapijpapractice.features.user.dto.Request.VerifyResetPasswordRequest;
import com.example.mbaningapijpapractice.features.user.dto.UserResponse;
import jakarta.mail.MessagingException;

import java.util.List;

public interface UserService {

    List<UserResponse> findAllUser();

    UserResponse findUserById(Integer id);


    //Find User by UUID
    UserResponse findUserByUuid(String uuid);


    //Find User by Phone Number
    UserResponse findUserByPhoneNumber(String PhoneNumber);

    //Create New User
    UserResponse createNewUser(CreateUserRequest createUserRequest);


    void resetPassword(ResetPasswordRequest resetPasswordRequest) throws MessagingException;

    void verifyResetPassword(VerifyResetPasswordRequest verifyResetPasswordRequest) ;

}
