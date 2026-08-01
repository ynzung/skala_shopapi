package com.sk.skala.shopapi.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String customerId,
        @NotBlank String customerPassword
) {
}
