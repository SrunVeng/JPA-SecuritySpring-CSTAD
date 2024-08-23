package com.example.mbaningapijpapractice.features.account;

import com.example.mbaningapijpapractice.features.account.dto.Response.AccountTypeResponse;

import java.util.List;

public interface AccountTypeService {

    List<AccountTypeResponse> findAllAccountType();

}
