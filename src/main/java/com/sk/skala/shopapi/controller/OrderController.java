package com.sk.skala.shopapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sk.skala.shopapi.dto.order.OrderItemResponse;
import com.sk.skala.shopapi.dto.order.OrderListResponse;
import com.sk.skala.shopapi.dto.order.OrderRequest;
import com.sk.skala.shopapi.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "주문 관리", description = "상품 주문 및 취소 API")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{customerId}")
    @Operation(summary = "고객 주문 상세 조회", description = "고객 정보와 주문 상품 목록을 조회합니다.")
    public ResponseEntity<OrderListResponse> getCustomerById(
            @PathVariable String customerId
    ) {
        return ResponseEntity.ok(orderService.getCustomerOrderList(customerId));
    }

    @GetMapping("/{customerId}/products")
    @Operation(summary = "고객 주문 상품 조회", description = "고객이 주문한 상품 목록을 조회합니다.")
    public ResponseEntity<List<OrderItemResponse>> getCustomerProducts(
            @PathVariable String customerId
    ) {
        return ResponseEntity.ok(orderService.getCustomerProducts(customerId));
    }

    @PostMapping("/order")
    @Operation(summary = "상품 주문", description = "상품을 주문하고 고객 포인트를 차감합니다.")
    public ResponseEntity<OrderListResponse> placeOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        OrderListResponse order = orderService.placeOrder(
                request.customerId(), request.productId(), request.quantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/cancel")
    @Operation(summary = "주문 취소", description = "주문 수량을 차감하고 포인트를 환급합니다.")
    public ResponseEntity<OrderListResponse> cancelOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(
                request.customerId(), request.productId(), request.quantity()
        ));
    }
}
