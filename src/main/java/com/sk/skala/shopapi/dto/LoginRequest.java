package com.sk.skala.shopapi.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String customerId,
        @NotBlank String customerPassword
) {
}
