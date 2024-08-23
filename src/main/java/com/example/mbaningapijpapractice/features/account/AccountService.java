package com.example.mbaningapijpapractice.features.account;

import com.example.mbaningapijpapractice.domain.Account;
import com.example.mbaningapijpapractice.features.account.dto.Request.CreateAccountRequest;
import com.example.mbaningapijpapractice.features.account.dto.Response.AccountDetailResponse;
import org.springframework.stereotype.Service;


public interface AccountService {

    void createAccount(CreateAccountRequest createAccountRequest);

    AccountDetailResponse findAccByActNo(String actNo);
}
