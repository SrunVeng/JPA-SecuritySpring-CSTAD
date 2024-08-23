package com.example.mbaningapijpapractice.features.user;

import com.example.mbaningapijpapractice.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
}
