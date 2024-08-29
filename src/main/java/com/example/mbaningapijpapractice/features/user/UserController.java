package com.example.mbaningapijpapractice.features.user;


import com.example.mbaningapijpapractice.features.card.CardTypeService;
import com.example.mbaningapijpapractice.features.card.dto.CardTypeResponse;
import com.example.mbaningapijpapractice.features.user.dto.Request.CreateUserRequest;
import com.example.mbaningapijpapractice.features.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    List<UserResponse> findAllUsers() {
        return userService.findAllUser();
    }


    @GetMapping("/id/{id}")
    UserResponse findUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }


    @GetMapping("/uuid/{uuid}")
    UserResponse findUserByUuid(@PathVariable String uuid) {
        return userService.findUserByUuid(uuid);
    }

    @PostMapping("/create")
    UserResponse createNewUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userService.createNewUser(createUserRequest);
    }

    @GetMapping("/phoneNo/{phoneNumber}")
    UserResponse findUserByPhoneNumber(@PathVariable String phoneNumber) {
        return userService.findUserByPhoneNumber(phoneNumber);
    }

}
