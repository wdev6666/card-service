package com.zbank.cardservice.repository;

import com.zbank.cardservice.entity.Card;
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

    boolean existsByPanAndCardType(
            String pan,
            String cardType
    );
}