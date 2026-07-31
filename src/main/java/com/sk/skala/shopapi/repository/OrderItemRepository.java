
package com.sk.skala.shopapi.repository;

import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.entity.OrderItem;
import com.sk.skala.shopapi.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByCustomerAndProduct(Customer customer, Product product);

    List<OrderItem> findAllByCustomer(Customer customer);
}