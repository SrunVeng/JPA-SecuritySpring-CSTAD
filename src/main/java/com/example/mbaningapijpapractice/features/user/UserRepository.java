package com.example.mbaningapijpapractice.features.user;

import com.example.mbaningapijpapractice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Detrived method
    Optional<User> findByPhoneNumber(String PhoneNumber);

    Optional<User> findByuuid(String uuid);

    Optional<User> findByemail(String email);

    Boolean existsBynationalCardId(String PhoneNumber);

    Boolean existsByPhoneNumber(String PhoneNumber);
    //JPQL


}
