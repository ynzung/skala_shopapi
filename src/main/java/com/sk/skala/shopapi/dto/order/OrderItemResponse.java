package com.sk.skala.shopapi.dto.order;

public record OrderItemResponse(
        Long productId,
        String productName,
        Double productPrice,
        Integer quantity
) {
}
