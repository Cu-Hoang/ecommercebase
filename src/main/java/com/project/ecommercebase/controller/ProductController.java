package com.project.ecommercebase.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.*;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.dto.response.ProductResponse;
import com.project.ecommercebase.service.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoint.ENDPOINT_PRODUCT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    ProductService productService;

    @PostMapping(Endpoint.ProductEndpoint.CREATE_PRODUCT)
    public ApiResponse<String> createProduct(
            @PathVariable("categoryId") UUID id, @RequestBody @Validated ProductRequest productRequest) {
        return ApiResponse.<String>builder()
                .message(productService.createProduct(productRequest, id))
                .build();
    }

    @PatchMapping(Endpoint.ProductEndpoint.UPDATE_PRODUCT)
    public ApiResponse<String> updateProduct(
            @PathVariable("productId") UUID id, @RequestBody @Validated UpdateProductRequest updateProductRequest) {
        return ApiResponse.<String>builder()
                .message(productService.updateProduct(updateProductRequest, id))
                .build();
    }

    @GetMapping(Endpoint.ProductEndpoint.GET_ALL_PRODUCTS_BY_VENDOR)
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProducts())
                .build();
    }

    @GetMapping(Endpoint.ProductEndpoint.GET_ALL_PRODUCTS_BY_CUSTOMER)
    public ApiResponse<List<ProductResponse>> getAllProducts(@PathVariable("shopId") UUID shopId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProducts(shopId))
                .build();
    }

    @GetMapping(Endpoint.ProductEndpoint.GET_PRODUCT)
    public ApiResponse<ProductResponse> getProduct(@PathVariable("productId") UUID productId) {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.getProduct(productId))
                .build();
    }

    @GetMapping(Endpoint.ProductEndpoint.GET_ALL_PRODUCTS_BY_CATEGORY_VENDOR)
    public ApiResponse<List<ProductResponse>> getProductByCategory(@PathVariable("categoryId") UUID categoryId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProductsByCategory(categoryId))
                .build();
    }

    @GetMapping(Endpoint.ProductEndpoint.GET_ALL_PRODUCTS_BY_CATEGORY_CUSTOMER)
    public ApiResponse<List<ProductResponse>> getProductByCategory(
            @PathVariable("categoryId") UUID categoryId, @PathVariable("shopId") UUID shopId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productService.getAllProductsByCategory(categoryId, shopId))
                .build();
    }

    @PostMapping(Endpoint.ProductEndpoint.CREATE_ATTRIBUTE)
    public ApiResponse<String> createAttribute(@RequestBody @Validated AttributeRequest attributeRequest) {
        return ApiResponse.<String>builder()
                .message(productService.createAttribute(attributeRequest))
                .build();
    }

    @PatchMapping(Endpoint.ProductEndpoint.UPDATE_ATTRIBUTE)
    public ApiResponse<String> updateAttribute(
            @PathVariable("attributeId") UUID id, @RequestBody @Validated AttributeRequest attributeRequest) {
        return ApiResponse.<String>builder()
                .message(productService.updateAttribute(id, attributeRequest))
                .build();
    }

    @PostMapping(Endpoint.ProductEndpoint.CREATE_ATTRIBUTE_VALUE)
    public ApiResponse<String> createAttributeValue(
            @PathVariable("attributeId") UUID id, @RequestBody @Validated ValueRequest valueRequest) {
        return ApiResponse.<String>builder()
                .message(productService.createAttributeValue(id, valueRequest))
                .build();
    }

    @PatchMapping(Endpoint.ProductEndpoint.UPDATE_ATTRIBUTE_VALUE)
    public ApiResponse<String> updateAttributeValue(
            @PathVariable("attributeValueId") UUID id, @RequestBody @Validated ValueRequest valueRequest) {
        return ApiResponse.<String>builder()
                .message(productService.updateAttributeValue(id, valueRequest))
                .build();
    }

    @DeleteMapping(Endpoint.ProductEndpoint.DELETE_ATTRIBUTE)
    public ApiResponse<String> deleteAttribute(@PathVariable("attributeId") UUID id) {
        return ApiResponse.<String>builder()
                .message(productService.deleteAttribute(id))
                .build();
    }

    @DeleteMapping(Endpoint.ProductEndpoint.DELETE_ATTRIBUTE_VALUE)
    public ApiResponse<String> deleteAttributeValue(@PathVariable("attributeValueId") UUID id) {
        return ApiResponse.<String>builder()
                .message(productService.deleteAttributeValue(id))
                .build();
    }

    @PostMapping(Endpoint.ProductEndpoint.ADD_ATTRIBUTE_VALUE_TO_PRODUCT)
    public ApiResponse<String> addAttributeValueToProduct(
            @RequestBody @Validated ProductAttributeValueRequest request) {
        return ApiResponse.<String>builder()
                .message(productService.addAttributeValueToProduct(request))
                .build();
    }

    @DeleteMapping(Endpoint.ProductEndpoint.DELETE_ATTRIBUTE_VALUE_FROM_PRODUCT)
    public ApiResponse<String> deleteAttributeValueFromProduct(
            @RequestBody @Validated ProductAttributeValueRequest request) {
        return ApiResponse.<String>builder()
                .message(productService.deleteAttributeValueFromProduct(request))
                .build();
    }
}
