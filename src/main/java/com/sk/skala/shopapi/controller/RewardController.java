package com.sk.skala.shopapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sk.skala.shopapi.dto.reward.CheckInResponse;
import com.sk.skala.shopapi.service.RewardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "보상 관리", description = "출석 및 포인트 보상 API")
public class RewardController {
    private final RewardService rewardService;

    @PostMapping("/{customerId}/check-in")
    @Operation(summary = "일일 출석 체크", description = "하루 한 번 1,000포인트를 지급합니다.")
    public ResponseEntity<CheckInResponse> checkIn(
            @PathVariable String customerId
    ) {
        return ResponseEntity.ok(rewardService.checkIn(customerId));
    }
}
