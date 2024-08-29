package com.example.mbaningapijpapractice.features.card;


import com.example.mbaningapijpapractice.features.card.dto.CardTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/card-types")
@RequiredArgsConstructor
public class CardTypeController {

    private final CardTypeService cardTypeService;

    @GetMapping
    List<CardTypeResponse> findAllCardType() {
        return cardTypeService.findAllCardTypes();
    }
}
