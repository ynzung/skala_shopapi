package com.sk.skala.shopapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// import java.time.LocalDateTime;

/**
 * 상품 정보를 저장하는 엔티티
 * 상품 id, 상품명, 상품가 상품 기본 정보를 관리합니다.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 상품 id

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName; // 상품명

    @Column(name = "product_price", nullable = false)
    private Double productPrice; // 상품가

}


