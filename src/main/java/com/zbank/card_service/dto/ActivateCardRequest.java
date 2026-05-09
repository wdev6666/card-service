package com.zbank.card_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActivateCardRequest {

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String pan;

    @Pattern(regexp = "\\d{4}", message = "Old PIN must be 4 digits")
    private String oldPin;

    @Pattern(regexp = "\\d{4}", message = "New PIN must be 4 digits")
    private String newPin;
}