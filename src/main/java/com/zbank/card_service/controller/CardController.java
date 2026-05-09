package com.zbank.card_service.controller;

import com.zbank.card_service.dto.ActivateCardRequest;
import com.zbank.card_service.dto.CardRequest;
import com.zbank.card_service.entity.Card;
import com.zbank.card_service.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Card APIs", description = "APIs for card generation")

public class CardController {

    private final CardService cardService;

    @PostMapping("/generate")
    @Operation(
            summary = "Generate credit card",
            description = "Creates a new credit card for given PAN"
    )
    public Card generateCard(@Valid @RequestBody CardRequest request) {
        return cardService.generateCard(request);
    }

    @PostMapping("/activate")
    @Operation(
            summary = "Activate card",
            description = "Activates credit card and sets PIN"
    )
    public Card activateCard(
            @Valid @RequestBody ActivateCardRequest request
    ) {
        return cardService.activateCard(request);
    }
}