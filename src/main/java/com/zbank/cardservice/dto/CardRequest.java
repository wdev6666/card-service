package com.zbank.cardservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CardRequest {

    @NotBlank
    private String pan;

    @NotBlank
    private String cardType;
}