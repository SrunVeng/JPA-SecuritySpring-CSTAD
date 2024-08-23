package com.example.mbaningapijpapractice.features.account;

import com.example.mbaningapijpapractice.domain.Account;
import com.example.mbaningapijpapractice.domain.AccountType;
import com.example.mbaningapijpapractice.domain.User;
import com.example.mbaningapijpapractice.domain.UserAccount;
import com.example.mbaningapijpapractice.features.account.dto.Request.CreateAccountRequest;
import com.example.mbaningapijpapractice.features.account.dto.Response.AccountDetailResponse;
import com.example.mbaningapijpapractice.features.user.UserAccountRepository;
import com.example.mbaningapijpapractice.features.user.UserRepository;
import com.example.mbaningapijpapractice.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final AccountMapper accountMapper;

    // Validate Existing ActNo Cannot Create

    @Override
    public void createAccount(CreateAccountRequest createAccountRequest) {

        if (accountRepository.existsByActNo(createAccountRequest.actNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account already exists");
        }

        AccountType accountType =
                accountTypeRepository.findByAlias
                                (createAccountRequest.alias()).
                        orElseThrow(() ->
                                new ResponseStatusException
                                        (HttpStatus.NOT_ACCEPTABLE, "Account type not found"));

        User user = userRepository.
                findByPhoneNumber(createAccountRequest.PhoneNumber()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Phone Number does not exist"));


        Account account = accountMapper.fromCreateAccountRequest(createAccountRequest);
        account.setIsHidden(false);
        account.setIsDeleted(false);
        account.setTransferLimit(BigDecimal.valueOf(5000));
        account.setAlias("");
        account.setAccountType(accountType);

        UserAccount userAccount = new UserAccount();
        userAccount.setCreatedAt(LocalDate.now());
        userAccount.setIsDeleted(false);
        userAccount.setIsBlocked(false);
        userAccount.setAccount(account);
        userAccount.setUser(user);
        userAccountRepository.save(userAccount);


    }

    @Override
    public AccountDetailResponse findAccByActNo(String actNo) {

        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account does not exist"));
        return accountMapper.toAccountDetailResponse(account);

    }
}
