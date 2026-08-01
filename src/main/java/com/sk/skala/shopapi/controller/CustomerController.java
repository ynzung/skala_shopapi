package com.sk.skala.shopapi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sk.skala.shopapi.dto.customer.CustomerResponse;
import com.sk.skala.shopapi.dto.customer.LoginRequest;
import com.sk.skala.shopapi.dto.customer.LoginResponse;
import com.sk.skala.shopapi.dto.customer.SignupRequest;
import com.sk.skala.shopapi.dto.customer.SignupResponse;
import com.sk.skala.shopapi.dto.customer.UpdateCustomerRequest;
import com.sk.skala.shopapi.service.CustomerService;
import com.sk.skala.shopapi.service.AuthService;

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
    private final AuthService authService;

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
    public ResponseEntity<LoginResponse> loginCustomer(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PutMapping("/me")
    @Operation(summary = "내 정보 수정", description = "로그인한 고객의 비밀번호를 수정합니다.")
    public ResponseEntity<CustomerResponse> updateCustomer(
            Principal principal,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        return ResponseEntity.ok(
                customerService.updateCustomer(principal.getName(), request)
        );
    }

    @DeleteMapping("/me")
    @Operation(summary = "회원 탈퇴", description = "로그인한 고객과 관련 정보를 삭제합니다.")
    public ResponseEntity<Void> deleteCustomer(Principal principal) {
        customerService.deleteCustomer(principal.getName());
        return ResponseEntity.noContent().build();
    }
}
