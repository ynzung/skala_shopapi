package com.sk.skala.shopapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sk.skala.shopapi.dto.favorite.FavoriteResponse;
import com.sk.skala.shopapi.service.FavoriteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "찜 관리", description = "상품 찜 추가 및 삭제 API")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/{customerId}/favorites/{productId}")
    @Operation(summary = "상품 찜하기", description = "고객의 찜 목록에 상품을 추가합니다.")
    public ResponseEntity<FavoriteResponse> addFavorite(
            @PathVariable String customerId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.addFavorite(customerId, productId));
    }

    @GetMapping("/{customerId}/favorites")
    @Operation(summary = "찜 목록 조회", description = "고객이 찜한 상품 목록을 조회합니다.")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            @PathVariable String customerId
    ) {
        return ResponseEntity.ok(favoriteService.getFavorites(customerId));
    }

    @DeleteMapping("/{customerId}/favorites/{productId}")
    @Operation(summary = "상품 찜 취소", description = "고객의 찜 목록에서 상품을 삭제합니다.")
    public ResponseEntity<Void> deleteFavorite(
            @PathVariable String customerId,
            @PathVariable Long productId
    ) {
        favoriteService.deleteFavorite(customerId, productId);
        return ResponseEntity.noContent().build();
    }
}
