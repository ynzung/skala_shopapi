package com.sk.skala.shopapi.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank(message = "상품명은 필수입니다.")
        String productName,

        @NotNull(message = "상품 가격은 필수입니다.")
        @Positive(message = "상품 가격은 0보다 커야 합니다.")
        Double productPrice
) {
}
