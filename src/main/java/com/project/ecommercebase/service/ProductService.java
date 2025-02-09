package com.project.ecommercebase.service;

import java.util.List;
import java.util.UUID;

import com.project.ecommercebase.dto.request.ProductRequest;
import com.project.ecommercebase.dto.request.UpdateProductRequest;
import com.project.ecommercebase.dto.response.ProductResponse;

public interface ProductService {
    String createProduct(ProductRequest productRequest, UUID categoryId);

    String updateProduct(UpdateProductRequest updateProductRequest, UUID productId);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getAllProducts(UUID shopId);

    ProductResponse getProduct(UUID id);

    List<ProductResponse> getAllProductsByCategory(UUID categoryId);

    List<ProductResponse> getAllProductsByCategory(UUID categoryId, UUID shopId);
}
