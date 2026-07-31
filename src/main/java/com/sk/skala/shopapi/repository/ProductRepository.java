package com.sk.skala.shopapi.repository;
import com.sk.skala.shopapi.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductName(String productName);

    Optional<Product> findByProductName(String productName);

}
