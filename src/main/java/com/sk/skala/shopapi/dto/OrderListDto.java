package com.sk.skala.shopapi.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderListDto {

    private String customerId;

    @NotNull(message = "고객 포인트는 필수입니다")
    private Double customerPoint;

    @NotNull(message = "주문 상품 목록은 필수입니다")
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