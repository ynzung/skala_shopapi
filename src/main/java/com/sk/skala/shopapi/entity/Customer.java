package com.sk.skala.shopapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 고객 정보를 저장하는 엔티티
 * 고객 아이디, 비밀번호, 포인트 정보를 관리합니다.
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @Column(nullable = false, length = 50, unique = true)
    private String customerId;    // 고객 아이디

    @Column(nullable = false, length = 255)
    private String customerPassword; // 고객 비밀번호

    @Column(nullable = false)
    private Double customerPoint; // 고객 포인트

}
