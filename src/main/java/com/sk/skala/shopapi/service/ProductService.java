package com.sk.skala.shopapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.ProductDto;
import com.sk.skala.shopapi.entity.Product;
import com.sk.skala.shopapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    // 1. 전체 상품 목록 조회
    public List<ProductDto> getAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // 2. 개별 상품 상세 조회
    public ProductDto getProductById(Long id) {
        Product product = findProductById(id);
        return convertToDto(product);
    }

    // 3. 상품 등록
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .productName(productDto.getProductName())
                .productPrice(productDto.getProductPrice())
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    // 4. 상품 정보 수정
    @Transactional
    public ProductDto updateProduct(
            Long id,
            ProductDto productDto
    ) {
        Product product = findProductById(id);

        product.setProductName(productDto.getProductName());
        product.setProductPrice(productDto.getProductPrice());

        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    // 5. 상품 삭제
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("상품을 찾을 수 없습니다: " + id);
        }

        productRepository.deleteById(id);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "상품을 찾을 수 없습니다: " + id
                        )
                );
    }

    private ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .build();
    }
}
