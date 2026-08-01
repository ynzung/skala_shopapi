package com.sk.skala.shopapi.dto.favorite;

public record FavoriteResponse(
        Long productId,
        String productName,
        Double productPrice
) {
}
