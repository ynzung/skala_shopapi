package com.sk.skala.shopapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sk.skala.shopapi.dto.CustomerDto;
import com.sk.skala.shopapi.dto.LoginRequest;
import com.sk.skala.shopapi.dto.OrderItemDto;
import com.sk.skala.shopapi.dto.OrderListDto;
import com.sk.skala.shopapi.dto.OrderRequest;
import com.sk.skala.shopapi.dto.SignupResponse;
import com.sk.skala.shopapi.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "고객 관리", description = "고객 CRUD API")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/list")
    @Operation(
            summary = "전체 고객 조회",
            description = "등록된 모든 고객 목록을 조회합니다."
    )
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomer();
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/{customerId}")
    @Operation(
            summary = "고객 상세 조회",
            description = "고객 정보와 고객이 주문한 상품 목록을 함께 조회합니다."
    )
    public ResponseEntity<OrderListDto> getCustomerById(
            @PathVariable String customerId
    ) {
        OrderListDto customer =
                customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{customerId}/products")
    @Operation(
            summary = "고객 주문 상품 정보 조회",
            description = "고객이 주문해 보유하고 있는 상품 목록을 조회합니다."
    )
    public ResponseEntity<List<OrderItemDto>> getCustomerProducts(
            @PathVariable String customerId
    ) {
        List<OrderItemDto> products =
                customerService.getCustomerProducts(customerId);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(
            summary = "고객 회원가입",
            description = "새로운 고객을 등록합니다."
    )
    public ResponseEntity<SignupResponse> createCustomer(
            @RequestBody CustomerDto customerDto
    ) {
        SignupResponse response =
                customerService.createCustomer(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "고객 로그인",
            description = "고객 아이디와 비밀번호를 확인합니다."
    )
    public ResponseEntity<CustomerDto> loginCustomer(
            @Valid @RequestBody LoginRequest request
    ) {
        CustomerDto customer = customerService.loginCustomer(
                request.customerId(),
                request.customerPassword()
        );
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{customerId}")
    @Operation(
            summary = "고객 정보 수정",
            description = "고객의 비밀번호와 포인트 정보를 수정합니다."
    )
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable String customerId,
            @RequestBody CustomerDto customerDto
    ) {
        CustomerDto updatedCustomer =
                customerService.updateCustomer(customerId, customerDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerId}")
    @Operation(
            summary = "고객 삭제",
            description = "고객과 해당 고객의 주문 정보를 삭제합니다."
    )
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable String customerId
    ) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/order")
    @Operation(
            summary = "고객 상품 주문",
            description = "고객이 상품을 주문하고 포인트를 차감합니다."
    )
    public ResponseEntity<OrderListDto> placeOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        OrderListDto order = customerService.placeOrder(
                request.customerId(),
                request.productId(),
                request.quantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/cancel")
    @Operation(
            summary = "고객 주문 취소",
            description = "주문 상품 수량을 취소하고 포인트를 환급합니다."
    )
    public ResponseEntity<OrderListDto> cancelOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        OrderListDto order = customerService.cancelOrder(
                request.customerId(),
                request.productId(),
                request.quantity()
        );
        return ResponseEntity.ok(order);
    }
}
