package com.example.mbaningapijpapractice.features.account;

import com.example.mbaningapijpapractice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

     Boolean existsByActNo(String ActNo);

     Optional<Account> findByActNo(String ActNo);

}
