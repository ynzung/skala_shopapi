package com.sk.skala.shopapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {
    private Long productId;

    private String productName;

    private Double productPrice;

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
