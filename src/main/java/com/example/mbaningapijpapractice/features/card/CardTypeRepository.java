package com.example.mbaningapijpapractice.features.card;

import com.example.mbaningapijpapractice.domain.CardType;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
}
