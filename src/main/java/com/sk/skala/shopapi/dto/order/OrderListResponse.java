package com.sk.skala.shopapi.dto.order;

import java.util.List;

public record OrderListResponse(
        String customerId,
        Double customerPoint,
        List<OrderItemResponse> products
) {
}
