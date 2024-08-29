package com.example.mbaningapijpapractice.features.account;


import com.example.mbaningapijpapractice.features.account.dto.Request.CreateAccountRequest;
import com.example.mbaningapijpapractice.features.account.dto.Response.AccountDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    void createAccount(@Valid @RequestBody CreateAccountRequest request) {
        accountService.createAccount(request);
    }

    @GetMapping("/{actNo}")
    AccountDetailResponse findAccByActNo(@PathVariable String actNo) {
        return accountService.findAccByActNo(actNo);
    }

}
