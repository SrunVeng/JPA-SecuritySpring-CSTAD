package com.example.mbaningapijpapractice.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "account_types")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String alias;
    @Column(columnDefinition="TEXT")
    private String description;



    private Boolean isDeleted;

    @Column(nullable = false, unique = true, length = 50)
    private String name;


    @OneToMany(mappedBy="accountType")
    private List<Account> accounts;




}
