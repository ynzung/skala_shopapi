package com.sk.skala.shopapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {
    private Long productId;

    @NotBlank(message = "상품명은 필수입니다")
    private String productName;

    @NotNull(message = "상품가는 필수입니다")
    private Double productPrice;

    @NotNull(message = "상품 수량은 필수입니다")
    private Integer quantity;

    @Builder
    public OrderItemDto(
            Long productId,
            String productName,
            Double productPrice,
            Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }


}
