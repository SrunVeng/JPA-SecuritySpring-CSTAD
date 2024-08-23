package com.example.mbaningapijpapractice.features.card;

import com.example.mbaningapijpapractice.features.card.dto.CardTypeResponse;

import java.util.List;

public interface CardTypeService {

    List<CardTypeResponse> findAllCardTypes();
}
