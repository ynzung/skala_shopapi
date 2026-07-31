package com.sk.skala.shopapi.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderListDto {

    private String customerId;

    private Double customerPoint;

    private List<OrderItemDto> products;

    @Builder
    public OrderListDto(
            String customerId,
            Double customerPoint,
            List<OrderItemDto> products) {
        this.customerId = customerId;
        this.customerPoint = customerPoint;
        this.products = products;
    }
}
