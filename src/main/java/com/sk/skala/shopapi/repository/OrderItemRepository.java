package com.sk.skala.shopapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.entity.OrderItem;
import com.sk.skala.shopapi.entity.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByCustomerCustomerId(String customerId);

    Optional<OrderItem> findByCustomerAndProduct(
            Customer customer,
            Product product
    );
}
