package com.sk.skala.shopapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.favorite.FavoriteResponse;
import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.entity.Favorite;
import com.sk.skala.shopapi.entity.Product;
import com.sk.skala.shopapi.exception.DataNotFoundException;
import com.sk.skala.shopapi.exception.DuplicateFavoriteException;
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
    public FavoriteResponse addFavorite(String customerId, Long productId) {
        Customer customer = findCustomerById(customerId);
        Product product = findProductById(productId);

        if (favoriteRepository
                .existsByCustomerCustomerIdAndProductId(customerId, productId)) {
            throw new DuplicateFavoriteException("이미 찜한 상품입니다.");
        }

        favoriteRepository.save(Favorite.builder()
                .customer(customer)
                .product(product)
                .build());
        return convertToDto(product);
    }

    public List<FavoriteResponse> getFavorites(String customerId) {
        findCustomerById(customerId);
        return favoriteRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(favorite -> convertToDto(favorite.getProduct()))
                .toList();
    }

    @Transactional
    public void deleteFavorite(String customerId, Long productId) {
        findCustomerById(customerId);
        Favorite favorite = favoriteRepository
                .findByCustomerCustomerIdAndProductId(customerId, productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "찜한 상품을 찾을 수 없습니다."
                ));
        favoriteRepository.delete(favorite);
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

    private FavoriteResponse convertToDto(Product product) {
        return new FavoriteResponse(
                product.getId(),
                product.getProductName(),
                product.getProductPrice()
        );
    }
}
