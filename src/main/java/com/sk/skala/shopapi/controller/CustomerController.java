package com.sk.skala.shopapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sk.skala.shopapi.dto.customer.CustomerResponse;
import com.sk.skala.shopapi.dto.customer.LoginRequest;
import com.sk.skala.shopapi.dto.customer.SignupRequest;
import com.sk.skala.shopapi.dto.customer.SignupResponse;
import com.sk.skala.shopapi.dto.customer.UpdateCustomerRequest;
import com.sk.skala.shopapi.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "고객 관리", description = "고객 CRUD API")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/list")
    @Operation(summary = "전체 고객 조회", description = "등록된 모든 고객 목록을 조회합니다.")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomer());
    }

    @PostMapping
    @Operation(summary = "고객 회원가입", description = "새로운 고객을 등록합니다.")
    public ResponseEntity<SignupResponse> createCustomer(
            @Valid @RequestBody SignupRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.createCustomer(request));
    }

    @PostMapping("/login")
    @Operation(summary = "고객 로그인", description = "고객 아이디와 비밀번호를 확인합니다.")
    public ResponseEntity<CustomerResponse> loginCustomer(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(customerService.loginCustomer(
                request.customerId(), request.customerPassword()
        ));
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "고객 정보 수정", description = "고객 비밀번호를 수정합니다.")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable String customerId,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        return ResponseEntity.ok(
                customerService.updateCustomer(customerId, request)
        );
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "고객 삭제", description = "고객과 관련 정보를 삭제합니다.")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable String customerId
    ) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
