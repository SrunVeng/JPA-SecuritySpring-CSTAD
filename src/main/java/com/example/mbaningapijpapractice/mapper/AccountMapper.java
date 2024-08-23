package com.example.mbaningapijpapractice.mapper;

import com.example.mbaningapijpapractice.domain.Account;
import com.example.mbaningapijpapractice.features.account.dto.Request.CreateAccountRequest;
import com.example.mbaningapijpapractice.features.account.dto.Response.AccountDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account fromCreateAccountRequest(CreateAccountRequest createAccountRequest);

    AccountDetailResponse toAccountDetailResponse(Account account);

}
