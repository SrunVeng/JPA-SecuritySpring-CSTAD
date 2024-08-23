package com.example.mbaningapijpapractice.features.account;

import com.example.mbaningapijpapractice.domain.AccountType;
import com.example.mbaningapijpapractice.mapper.AccountTypeMapper;
import com.example.mbaningapijpapractice.features.account.dto.Response.AccountTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;
    private final AccountTypeMapper accountTypeMapper;


    @Override
    public List<AccountTypeResponse> findAllAccountType() {

        List<AccountType> accountTypes = accountTypeRepository.findAll();
        return accountTypeMapper.toAccountTypeResponse(accountTypes);

    }
}
