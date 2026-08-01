package com.sk.skala.shopapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.CustomerDto;
import com.sk.skala.shopapi.dto.OrderItemDto;
import com.sk.skala.shopapi.dto.OrderListDto;
import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.entity.OrderItem;
import com.sk.skala.shopapi.entity.Product;
import com.sk.skala.shopapi.exception.DataNotFoundException;
import com.sk.skala.shopapi.exception.DuplicateCustomerException;
import com.sk.skala.shopapi.exception.InsufficientFundsException;
import com.sk.skala.shopapi.exception.ParameterException;
import com.sk.skala.shopapi.repository.CustomerProductRepository;
import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerProductRepository customerProductRepository;

    // 1. 전체 고객 목록 조회
    public List<CustomerDto> getAllCustomer() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // 2. 단일 고객 및 상품 목록 조회
    public OrderListDto getCustomerById(String customerId) {
        Customer customer = findCustomerById(customerId);
        return convertToOrderListDto(customer);
    }

    // 고객의 상품 정보 조회
    public List<OrderItemDto> getCustomerProducts(String customerId) {
        findCustomerById(customerId);

        return customerProductRepository
                .findByCustomerCustomerId(customerId)
                .stream()
                .map(this::convertToOrderItemDto)
                .toList();
    }

    // 3. 고객 생성
    @Transactional
    public SignupResponse createCustomer(CustomerDto customerDto) {
        if (customerRepository.existsById(customerDto.getCustomerId())) {
            throw new DuplicateCustomerException(
                    "이미 존재하는 고객 아이디입니다: "
                            + customerDto.getCustomerId()
            );
        }

        Customer customer = Customer.builder()
                .customerId(customerDto.getCustomerId())
                .customerPassword(customerDto.getCustomerPassword())
                .customerPoint(customerDto.getCustomerPoint())
                .build();

        return convertToDto(customerRepository.save(customer));
    }

    // 4. 고객 로그인
    public CustomerDto loginCustomer(
            String customerId,
            String customerPassword
    ) {
        Customer customer = findCustomerById(customerId);

        if (!customer.getCustomerPassword().equals(customerPassword)) {
            throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return convertToDto(customer);
    }

    // 5. 고객 정보 업데이트
    @Transactional
    public CustomerDto updateCustomer(
            String customerId,
            CustomerDto customerDto
    ) {
        Customer customer = findCustomerById(customerId);

        if (customerDto.getCustomerPassword() != null
                && !customerDto.getCustomerPassword().isBlank()) {
            customer.setCustomerPassword(
                    customerDto.getCustomerPassword()
            );
        }
        if (customerDto.getCustomerPoint() != null) {
            customer.setCustomerPoint(customerDto.getCustomerPoint());
        }

        return convertToDto(customerRepository.save(customer));
    }

    // 6. 고객 삭제
    @Transactional
    public void deleteCustomer(String customerId) {
        Customer customer = findCustomerById(customerId);
        List<OrderItem> orderItems =
                customerProductRepository.findByCustomerCustomerId(customerId);

        customerProductRepository.deleteAll(orderItems);
        customerRepository.delete(customer);
    }

    // 7. 상품 주문
    @Transactional
    public OrderListDto placeOrder(
            String customerId,
            Long productId,
            int quantity
    ) {
        validateQuantity(quantity);

        Customer customer = findCustomerById(customerId);
        Product product = findProductById(productId);
        double orderPrice = product.getProductPrice() * quantity;

        if (customer.getCustomerPoint() < orderPrice) {
            throw new InsufficientFundsException("고객 포인트가 부족합니다.");
        }

        OrderItem orderItem = customerProductRepository
                .findByCustomerAndProduct(customer, product)
                .orElseGet(
                        () -> OrderItem.builder()
                                .customer(customer)
                                .product(product)
                                .quantity(0)
                                .build()
                );

        orderItem.setQuantity(orderItem.getQuantity() + quantity);
        customer.setCustomerPoint(customer.getCustomerPoint() - orderPrice);

        customerProductRepository.save(orderItem);
        customerRepository.save(customer);

        return convertToOrderListDto(customer);
    }

    // 8. 주문 취소
    @Transactional
    public OrderListDto cancelOrder(
            String customerId,
            Long productId,
            int quantity
    ) {
        validateQuantity(quantity);

        Customer customer = findCustomerById(customerId);
        Product product = findProductById(productId);
        OrderItem orderItem = customerProductRepository
                .findByCustomerAndProduct(customer, product)
                .orElseThrow(
                        () -> new DataNotFoundException(
                                "취소할 주문 상품을 찾을 수 없습니다."
                        )
                );

        if (orderItem.getQuantity() < quantity) {
            throw new ParameterException("보유 수량보다 많이 취소할 수 없습니다.");
        }

        int remainingQuantity = orderItem.getQuantity() - quantity;
        customer.setCustomerPoint(
                customer.getCustomerPoint()
                        + product.getProductPrice() * quantity
        );

        if (remainingQuantity == 0) {
            customerProductRepository.delete(orderItem);
        } else {
            orderItem.setQuantity(remainingQuantity);
            customerProductRepository.save(orderItem);
        }
        customerRepository.save(customer);

        return convertToOrderListDto(customer);
    }

    private Customer findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new DataNotFoundException(
                                "고객을 찾을 수 없습니다: " + customerId
                        )
                );
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(
                        () -> new DataNotFoundException(
                                "상품을 찾을 수 없습니다: " + productId
                        )
                );
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new ParameterException(
                    "상품 수량은 1 이상이어야 합니다."
            );
        }
    }

    private CustomerDto convertToDto(Customer customer) {
        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .customerPoint(customer.getCustomerPoint())
                .build();
    }

    private OrderListDto convertToOrderListDto(Customer customer) {
        List<OrderItemDto> products = customerProductRepository
                .findByCustomerCustomerId(customer.getCustomerId())
                .stream()
                .map(this::convertToOrderItemDto)
                .toList();

        return OrderListDto.builder()
                .customerId(customer.getCustomerId())
                .customerPoint(customer.getCustomerPoint())
                .products(products)
                .build();
    }

    private OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        Product product = orderItem.getProduct();

        return OrderItemDto.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
