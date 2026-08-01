package com.sk.skala.shopapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.product.ProductRequest;
import com.sk.skala.shopapi.dto.product.ProductResponse;
import com.sk.skala.shopapi.entity.Product;
import com.sk.skala.shopapi.exception.DataNotFoundException;
import com.sk.skala.shopapi.repository.ProductRepository;
import com.sk.skala.shopapi.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;

    // 1. 전체 상품 목록 조회
    public List<ProductResponse> getAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // 2. 개별 상품 상세 조회
    public ProductResponse getProductById(Long id) {
        Product product = findProductById(id);
        return convertToDto(product);
    }

    // 3. 상품 등록
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .productName(request.productName())
                .productPrice(request.productPrice())
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    // 4. 상품 정보 수정
    @Transactional
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request
    ) {
        Product product = findProductById(id);

        product.setProductName(request.productName());
        product.setProductPrice(request.productPrice());

        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    // 5. 상품 삭제
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new DataNotFoundException("상품을 찾을 수 없습니다: " + id);
        }

        favoriteRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new DataNotFoundException(
                                "상품을 찾을 수 없습니다: " + id
                        )
                );
    }

    private ProductResponse convertToDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getProductPrice()
        );
    }
}
