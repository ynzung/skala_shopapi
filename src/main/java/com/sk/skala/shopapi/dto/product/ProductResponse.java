package com.sk.skala.shopapi.dto.product;

public record ProductResponse(
        Long id,
        String productName,
        Double productPrice
) {
}
