package com.sk.skala.shopapi.controller;

import java.security.Principal;
import java.util.List;

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
@Tag(name = "찜 관리", description = "상품 찜 상태 관리 API")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/favorites/{productId}")
    @Operation(summary = "상품 찜 상태 변경", description = "호출할 때마다 상품의 찜 상태를 반전합니다.")
    public ResponseEntity<FavoriteResponse> toggleFavorite(
            Principal principal,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                favoriteService.toggleFavorite(principal.getName(), productId)
        );
    }

    @GetMapping("/favorites")
    @Operation(summary = "찜 목록 조회", description = "고객이 찜한 상품 목록을 조회합니다.")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            Principal principal
    ) {
        return ResponseEntity.ok(
                favoriteService.getFavorites(principal.getName())
        );
    }
}
