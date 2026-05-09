package com.zbank.cardservice.service;

import com.zbank.cardservice.dto.ActivateCardRequest;
import com.zbank.cardservice.dto.CardRequest;
import com.zbank.cardservice.entity.Card;
import com.zbank.cardservice.repository.CardRepository;
import com.zbank.cardservice.util.CardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public Card generateCard(CardRequest request) {

        boolean alreadyExists =
                cardRepository.existsByPanAndCardType(
                        request.getPan(),
                        request.getCardType()
                );

        if (alreadyExists) {
            throw new RuntimeException(
                    "Card already exists for this PAN and card type"
            );
        }

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
                .pin(CardUtil.generatePin())
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