package com.sk.skala.shopapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.shopapi.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByCustomerCustomerIdAndProductId(String customerId, Long productId);

    List<Favorite> findByCustomerCustomerId(String customerId);

    Optional<Favorite> findByCustomerCustomerIdAndProductId(
            String customerId,
            Long productId
    );

    void deleteByCustomerCustomerId(String customerId);

    void deleteByProductId(Long productId);
}
