package com.zbank.card_service.service;

import com.zbank.card_service.dto.ActivateCardRequest;
import com.zbank.card_service.dto.CardRequest;
import com.zbank.card_service.entity.Card;
import com.zbank.card_service.repository.CardRepository;
import com.zbank.card_service.util.CardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public Card generateCard(CardRequest request) {

        String cardNumber;

        do {
            cardNumber = CardUtil.generateCardNumber();
        } while (cardRepository.existsByCardNumber(cardNumber));

        double limit = switch (request.getCardType().toUpperCase()) {
            case "PLATINUM" -> 40000;
            case "GOLD" -> 20000;
            default -> 10000;
        };

        Card card = Card.builder()
                .pan(request.getPan())
                .cardNumber(cardNumber)
                .cardType(request.getCardType())
                .cardLimit(limit)
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(5))
                .cvv(CardUtil.generateCVV())
                .pin("0000")
                .firstTimeLogin(true)
                .build();

        return cardRepository.save(card);
    }


    public Card activateCard(ActivateCardRequest request) {

        Card card = cardRepository
                .findByCardNumberAndPanAndPin(
                        request.getCardNumber(),
                        request.getPan(),
                        request.getOldPin()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid card number, PAN or old PIN"
                        ));

        card.setPin(request.getNewPin());
        card.setFirstTimeLogin(false);

        return cardRepository.save(card);
    }
}