package com.zbank.cardservice.service;

import com.zbank.cardservice.dto.ActivateCardRequest;
import com.zbank.cardservice.dto.CardRequest;
import com.zbank.cardservice.entity.Card;
import com.zbank.cardservice.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private CardRequest cardRequest;

    @BeforeEach
    void setup() {

        cardRequest = new CardRequest();
        cardRequest.setPan("ABCDE1234F");
        cardRequest.setCardType("GOLD");
    }

    @Test
    void shouldGenerateCardSuccessfully() {

        when(cardRepository.existsByCardNumber(anyString()))
                .thenReturn(false);

        when(cardRepository.save(any(Card.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Card card = cardService.generateCard(cardRequest);

        assertNotNull(card);
        assertEquals("ABCDE1234F", card.getPan());
        assertEquals("GOLD", card.getCardType());
        assertEquals(20000, card.getCardLimit());

        verify(cardRepository, times(1))
                .save(any(Card.class));
    }

    @Test
    void shouldActivateCardSuccessfully() {

        ActivateCardRequest request =
                new ActivateCardRequest();

        request.setCardNumber("4578123412341234");
        request.setPan("ABCDE1234F");
        request.setOldPin("0000");
        request.setNewPin("1234");

        Card existingCard = Card.builder()
                .cardNumber(request.getCardNumber())
                .pan(request.getPan())
                .pin("0000")
                .firstTimeLogin(true)
                .build();

        when(cardRepository.findByCardNumberAndPanAndPin(
                request.getCardNumber(),
                request.getPan(),
                request.getOldPin()
        )).thenReturn(Optional.of(existingCard));

        when(cardRepository.save(any(Card.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Card updatedCard = cardService.activateCard(request);

        assertEquals("1234", updatedCard.getPin());
        assertFalse(updatedCard.getFirstTimeLogin());

        verify(cardRepository, times(1))
                .save(any(Card.class));
    }

    @Test
    void shouldThrowExceptionForInvalidOldPin() {

        ActivateCardRequest request =
                new ActivateCardRequest();

        request.setCardNumber("4578123412341234");
        request.setPan("ABCDE1234F");
        request.setOldPin("9999");

        when(cardRepository.findByCardNumberAndPanAndPin(
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> cardService.activateCard(request)
        );

        assertEquals(
                "Invalid card number, PAN or old PIN",
                ex.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenDuplicateCardExists() {

        when(cardRepository.existsByPanAndCardType(
                "ABCDE1234F",
                "GOLD"
        )).thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> cardService.generateCard(cardRequest)
        );

        assertEquals(
                "Card already exists for this PAN and card type",
                ex.getMessage()
        );
    }
}