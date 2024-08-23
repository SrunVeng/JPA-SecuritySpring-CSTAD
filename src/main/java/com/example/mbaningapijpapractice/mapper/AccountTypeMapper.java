package com.example.mbaningapijpapractice.mapper;


import com.example.mbaningapijpapractice.domain.AccountType;
import com.example.mbaningapijpapractice.features.account.dto.Response.AccountTypeResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    List<AccountTypeResponse> toAccountTypeResponse(List<AccountType> accountTypes);

}
