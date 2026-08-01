package com.sk.skala.shopapi.dto.reward;

public record CheckInResponse(
        String message,
        Double rewardPoint,
        Double customerPoint
) {
}
