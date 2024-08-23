package com.example.mbaningapijpapractice.features.account;


import com.example.mbaningapijpapractice.domain.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {

    Optional<AccountType> findByAlias(String alias);

}
