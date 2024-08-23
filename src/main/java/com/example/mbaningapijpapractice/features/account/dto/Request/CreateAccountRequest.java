package com.example.mbaningapijpapractice.features.account.dto.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateAccountRequest(

        @NotBlank(message = "Account No is required")
        @Size(min = 9, max = 9, message = "Invalid Account No")
        String actNo,
        @Positive(message = "Amount Must greater than 0")
        @NotNull(message = "Min Deposit 10 USD")
        BigDecimal balance,
        @NotNull
        @NotBlank(message = "Account type is required")
        String alias,
        @NotBlank(message = "Phone number is required")
        String PhoneNumber

) {
}
