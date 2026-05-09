package com.zbank.card_service.repository;

import com.zbank.card_service.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    boolean existsByCardNumber(String cardNumber);

    Optional<Card> findByCardNumberAndPan(
            String cardNumber,
            String pan
    );

    Optional<Card> findByCardNumberAndPanAndPin(
            String cardNumber,
            String pan,
            String pin
    );
}