package com.sk.skala.shopapi.dto;

public record CheckInResponse(
        String message,
        Double rewardPoint,
        Double customerPoint
) {
}
