package com.sk.skala.shopapi.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.customer.LoginRequest;
import com.sk.skala.shopapi.dto.customer.LoginResponse;
import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.exception.DataNotFoundException;
import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.customerId(), request.customerPassword()
                )
        );

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new DataNotFoundException("고객을 찾을 수 없습니다."));

        return new LoginResponse(
                jwtTokenProvider.createToken(customer.getCustomerId()),
                "Bearer",
                customer.getCustomerId(),
                customer.getCustomerPoint()
        );
    }
}
