package com.example.mbaningapijpapractice.mapper;


import com.example.mbaningapijpapractice.domain.CardType;
import com.example.mbaningapijpapractice.features.card.dto.CardTypeResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardTypeMapper {

    List<CardTypeResponse> toCardTypeResponse(List<CardType> cardTypes);

}
