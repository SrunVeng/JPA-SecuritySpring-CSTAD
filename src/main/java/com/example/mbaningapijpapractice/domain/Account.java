package com.example.mbaningapijpapractice.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ActName;

    @Column(length = 9, unique = true, nullable = false)
    private String actNo;

    private String alias;
    private BigDecimal balance;
    private Boolean isHidden;
    private Boolean isDeleted;
    private BigDecimal transferLimit;

    @ManyToOne
    private AccountType accountType;

    @OneToOne
    private Card card;

    @OneToMany(mappedBy = "account")
    private List<UserAccount> userAccounts;

    @OneToMany(mappedBy = "ownerId")
    private List<Transaction> transactionOwner;

}
