package com.example.mbaningapijpapractice.features.account.dto.Response;

import java.math.BigDecimal;

public record AccountDetailResponse(
        String alias,
        String actNo,
        BigDecimal balance,
        BigDecimal transferLimit,
        Boolean isHidden
) {
    // Additional methods, such as validation or formatted output, can be added here.
}
