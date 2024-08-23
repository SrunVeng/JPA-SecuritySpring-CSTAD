package com.example.mbaningapijpapractice.features.card;


import com.example.mbaningapijpapractice.domain.CardType;
import com.example.mbaningapijpapractice.mapper.CardTypeMapper;
import com.example.mbaningapijpapractice.features.card.dto.CardTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardTypeServiceImpl implements CardTypeService {


    private final CardTypeRepository cardTypeRepository;
    private final CardTypeMapper cardTypeMapper;

    @Override
    public List<CardTypeResponse> findAllCardTypes() {

        List<CardType> cardTypes = cardTypeRepository.findAll();

        return cardTypeMapper.toCardTypeResponse(cardTypes);

    }
}
