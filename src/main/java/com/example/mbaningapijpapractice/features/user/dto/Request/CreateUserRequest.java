package com.example.mbaningapijpapractice.features.user.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.LocalDate;

public record CreateUserRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String name,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotBlank(message = "National ID is required")
        String nationalCardId,
        @NotBlank(message = "Gender is required")
        String gender,


        String SignalId,
        String StudentIdCard,
        String cityOrProvince,
        String companyName,
        String employeeType,
        String khanOrDistrict,
        String mainSourceOfIncome,
        String monthlyIncomeRange,
        String position,
        String sangkatOrCommune,
        String street,
        String village,

        @NotBlank(message = "Pin is required")
        String pin,
        @NotBlank(message = "password is required")
        String password,
        String profileImage,
        @NotNull(message = "Date of Birth is required")
        LocalDate dateOfBirth
) {
}
