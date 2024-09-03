package com.example.mbaningapijpapractice.features.account;
import com.example.mbaningapijpapractice.features.account.dto.Request.CreateAccountRequest;
import com.example.mbaningapijpapractice.features.account.dto.Response.AccountDetailResponse;



public interface AccountService {

    void createAccount(CreateAccountRequest createAccountRequest);

    AccountDetailResponse findAccByActNo(String actNo);
}
