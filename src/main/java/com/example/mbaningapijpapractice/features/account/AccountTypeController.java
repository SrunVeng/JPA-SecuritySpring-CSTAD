package com.example.mbaningapijpapractice.features.account;


import com.example.mbaningapijpapractice.features.account.dto.Response.AccountTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @GetMapping
    public ResponseEntity<List<AccountTypeResponse>> findAllAccountTypes() {
        return new ResponseEntity<>(accountTypeService.findAllAccountType(), HttpStatus.OK);
    }


}
