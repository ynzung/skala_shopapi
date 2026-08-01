package com.sk.skala.shopapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.favorite.FavoriteResponse;
import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.entity.Favorite;
import com.sk.skala.shopapi.entity.Product;
import com.sk.skala.shopapi.exception.DataNotFoundException;
import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.repository.FavoriteRepository;
import com.sk.skala.shopapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public FavoriteResponse toggleFavorite(String customerId, Long productId) {
        Customer customer = findCustomerById(customerId);
        Product product = findProductById(productId);
        Favorite favorite = favoriteRepository
                .findByCustomerCustomerIdAndProductId(customerId, productId)
                .orElseGet(() -> Favorite.builder()
                        .customer(customer)
                        .product(product)
                        .favorite(true)
                        .build());

        if (favorite.getId() != null) {
            favorite.toggle();
        }
        favoriteRepository.save(favorite);
        return convertToDto(favorite);
    }

    public List<FavoriteResponse> getFavorites(String customerId) {
        findCustomerById(customerId);
        return favoriteRepository.findByCustomerCustomerIdAndFavoriteTrue(customerId)
                .stream()
                .map(this::convertToDto)
                .toList();
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

    private FavoriteResponse convertToDto(Favorite favorite) {
        Product product = favorite.getProduct();
        return new FavoriteResponse(
                product.getId(),
                product.getProductName(),
                product.getProductPrice(),
                favorite.isFavorite()
        );
    }
}
