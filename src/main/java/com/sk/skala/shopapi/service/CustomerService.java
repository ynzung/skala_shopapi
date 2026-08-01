package com.sk.skala.shopapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sk.skala.shopapi.dto.customer.CustomerResponse;
import com.sk.skala.shopapi.dto.customer.SignupRequest;
import com.sk.skala.shopapi.dto.customer.SignupResponse;
import com.sk.skala.shopapi.dto.customer.UpdateCustomerRequest;
import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.entity.OrderItem;
import com.sk.skala.shopapi.exception.DataNotFoundException;
import com.sk.skala.shopapi.exception.DuplicateCustomerException;
import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.repository.DailyCheckInRepository;
import com.sk.skala.shopapi.repository.FavoriteRepository;
import com.sk.skala.shopapi.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private static final double SIGNUP_BONUS_POINT = 3000.0;

    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final DailyCheckInRepository dailyCheckInRepository;
    private final FavoriteRepository favoriteRepository;
    private final PasswordEncoder passwordEncoder;

    public List<CustomerResponse> getAllCustomer() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public SignupResponse createCustomer(SignupRequest request) {
        if (customerRepository.existsById(request.customerId())) {
            throw new DuplicateCustomerException(
                    "이미 존재하는 고객 아이디입니다: "
                            + request.customerId()
            );
        }

        Customer customer = Customer.builder()
                .customerId(request.customerId())
                .customerPassword(passwordEncoder.encode(request.customerPassword()))
                .customerPoint(SIGNUP_BONUS_POINT)
                .build();
        customerRepository.save(customer);

        return new SignupResponse(
                "회원가입에 성공했습니다.",
                SIGNUP_BONUS_POINT
        );
    }

    @Transactional
    public CustomerResponse updateCustomer(
            String customerId,
            UpdateCustomerRequest request
    ) {
        Customer customer = findCustomerById(customerId);
        customer.setCustomerPassword(passwordEncoder.encode(request.customerPassword()));
        return convertToDto(customerRepository.save(customer));
    }

    @Transactional
    public void deleteCustomer(String customerId) {
        Customer customer = findCustomerById(customerId);
        List<OrderItem> orderItems =
                orderItemRepository.findByCustomerCustomerId(customerId);

        orderItemRepository.deleteAll(orderItems);
        dailyCheckInRepository.deleteByCustomerCustomerId(customerId);
        favoriteRepository.deleteByCustomerCustomerId(customerId);
        customerRepository.delete(customer);
    }

    private Customer findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException(
                        "고객을 찾을 수 없습니다: " + customerId
                ));
    }

    private CustomerResponse convertToDto(Customer customer) {
        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getCustomerPoint()
        );
    }
}
