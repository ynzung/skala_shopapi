package com.sk.skala.shopapi.repository;
import com.sk.skala.shopapi.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
