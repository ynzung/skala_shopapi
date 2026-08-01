package com.sk.skala.shopapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.order.OrderItemResponse;
import com.sk.skala.shopapi.dto.order.OrderListResponse;
import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.entity.OrderItem;
import com.sk.skala.shopapi.entity.Product;
import com.sk.skala.shopapi.exception.DataNotFoundException;
import com.sk.skala.shopapi.exception.InsufficientFundsException;
import com.sk.skala.shopapi.exception.ParameterException;
import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.repository.OrderItemRepository;
import com.sk.skala.shopapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderListResponse getCustomerOrderList(String customerId) {
        return convertToOrderListDto(findCustomerById(customerId));
    }

    public List<OrderItemResponse> getCustomerProducts(String customerId) {
        findCustomerById(customerId);
        return orderItemRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(this::convertToOrderItemDto)
                .toList();
    }

    @Transactional
    public OrderListResponse placeOrder(String customerId, Long productId, int quantity) {
        validateQuantity(quantity);
        Customer customer = findCustomerById(customerId);
        Product product = findProductById(productId);
        double orderPrice = product.getProductPrice() * quantity;

        if (customer.getCustomerPoint() < orderPrice) {
            throw new InsufficientFundsException("고객 포인트가 부족합니다.");
        }

        OrderItem orderItem = orderItemRepository
                .findByCustomerAndProduct(customer, product)
                .orElseGet(() -> OrderItem.builder()
                        .customer(customer)
                        .product(product)
                        .quantity(0)
                        .build());

        orderItem.setQuantity(orderItem.getQuantity() + quantity);
        customer.setCustomerPoint(customer.getCustomerPoint() - orderPrice);
        orderItemRepository.save(orderItem);
        customerRepository.save(customer);

        return convertToOrderListDto(customer);
    }

    @Transactional
    public OrderListResponse cancelOrder(String customerId, Long productId, int quantity) {
        validateQuantity(quantity);
        Customer customer = findCustomerById(customerId);
        Product product = findProductById(productId);
        OrderItem orderItem = orderItemRepository
                .findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new DataNotFoundException(
                        "취소할 주문 상품을 찾을 수 없습니다."
                ));

        if (orderItem.getQuantity() < quantity) {
            throw new ParameterException("보유 수량보다 많이 취소할 수 없습니다.");
        }

        int remainingQuantity = orderItem.getQuantity() - quantity;
        customer.setCustomerPoint(
                customer.getCustomerPoint() + product.getProductPrice() * quantity
        );

        if (remainingQuantity == 0) {
            orderItemRepository.delete(orderItem);
        } else {
            orderItem.setQuantity(remainingQuantity);
            orderItemRepository.save(orderItem);
        }
        customerRepository.save(customer);

        return convertToOrderListDto(customer);
    }

    private Customer findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException(
                        "고객을 찾을 수 없습니다: " + customerId
                ));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "상품을 찾을 수 없습니다: " + productId
                ));
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new ParameterException("상품 수량은 1 이상이어야 합니다.");
        }
    }

    private OrderListResponse convertToOrderListDto(Customer customer) {
        List<OrderItemResponse> products = orderItemRepository
                .findByCustomerCustomerId(customer.getCustomerId())
                .stream()
                .map(this::convertToOrderItemDto)
                .toList();

        return new OrderListResponse(
                customer.getCustomerId(),
                customer.getCustomerPoint(),
                products
        );
    }

    private OrderItemResponse convertToOrderItemDto(OrderItem orderItem) {
        Product product = orderItem.getProduct();
        return new OrderItemResponse(
                product.getId(),
                product.getProductName(),
                product.getProductPrice(),
                orderItem.getQuantity()
        );
    }
}
