package com.example.mbaningapijpapractice.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_accounts")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Account account;


    private Boolean isBlocked;
    private Boolean isDeleted;
    private LocalDate createdAt;

}
