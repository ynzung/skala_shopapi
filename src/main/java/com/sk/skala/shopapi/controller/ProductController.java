package com.sk.skala.shopapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sk.skala.shopapi.dto.ProductDto;
import com.sk.skala.shopapi.service.ProductService;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "상품 관리", description = "상품 CRUD API")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(
            summary = "전체 상품 조회",
            description = "등록된 모든 상품 목록을 조회합니다."
    )
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProduct();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    @Operation(
            summary = "상품 상세 조회",
            description = "상품 ID로 상품 상세 정보를 조회합니다."
    )
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(
            summary = "상품 등록",
            description = "새로운 상품을 등록합니다."
    )
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductDto productDto
    ) {
        ProductDto createdProduct =
                productService.createProduct(productDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }
    
    @PutMapping("/{id}")
    @Operation(
            summary = "상품 정보 수정",
            description = "상품 ID에 해당하는 상품명과 가격을 수정합니다."
    )
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto productDto
    ) {
        ProductDto updatedProduct =
                productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "상품 삭제",
            description = "상품 ID에 해당하는 상품을 삭제합니다."
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
