package com.sk.skala.shopapi.dto;

public record FavoriteDto(
        Long productId,
        String productName,
        Double productPrice
) {
}
