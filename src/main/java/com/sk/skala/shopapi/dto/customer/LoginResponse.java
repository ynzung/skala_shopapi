package com.sk.skala.shopapi.dto.customer;

public record LoginResponse(
        String accessToken,
        String tokenType,
        String customerId,
        Double customerPoint
) {
}
