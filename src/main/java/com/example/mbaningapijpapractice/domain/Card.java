package com.example.mbaningapijpapractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime expiryAt;
    private Boolean isDeleted;
    private LocalDateTime issuedAt;

    @OneToOne(mappedBy = "card")
    private Account account;

    @Column(nullable = false, length = 3)
    private String cvv;

    @Column(length = 100)
    private String holder;

    @Column(nullable = false, unique = true, length = 16)
    private String number;


    @ManyToOne
    private CardType cardtype;


}
