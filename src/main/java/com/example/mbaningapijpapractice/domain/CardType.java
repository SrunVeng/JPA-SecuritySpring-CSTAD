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
@Table(name = "card_types")
public class CardType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @Column(nullable = false, unique = true, length = 25)
    private String alias;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy="cardtype")
    private List<Card> cards;



}
