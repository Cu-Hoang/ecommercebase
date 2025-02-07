package com.project.ecommercebase.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.CategoryRequest;
import com.project.ecommercebase.dto.request.UpdateCategoryRequest;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.dto.response.CategoryResponse;
import com.project.ecommercebase.dto.response.FlatCategoryResponse;
import com.project.ecommercebase.service.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoint.ENDPOINT_CATEGORY)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @PostMapping(Endpoint.CategoryEndpoint.CREATE_CATEGORY)
    public ApiResponse<String> createCategory(@RequestBody @Validated CategoryRequest categoryRequest) {
        return ApiResponse.<String>builder()
                .message(categoryService.createCategory(categoryRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories())
                .build();
    }

    @GetMapping(Endpoint.CategoryEndpoint.GET_BY_SHOPID)
    public ApiResponse<List<CategoryResponse>> getAllCategories(@PathVariable("shopId") UUID shopId) {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories(shopId))
                .build();
    }

    @GetMapping(Endpoint.CategoryEndpoint.GET_BY_ID)
    public ApiResponse<FlatCategoryResponse> getCategoryById(@PathVariable("id") UUID id) {
        return ApiResponse.<FlatCategoryResponse>builder()
                .data(categoryService.getCategoryById(id))
                .build();
    }

    @GetMapping(Endpoint.CategoryEndpoint.GET_ALL_SUBCATEGORY_BY_ID)
    public ApiResponse<CategoryResponse> getAllSubCategoryByParentId(@PathVariable("id") UUID id) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getAllSubCategoryByParentId(id))
                .build();
    }

    @GetMapping(Endpoint.CategoryEndpoint.GET_ALL_SUBCATEGORY_BY_ID_AND_SHOPID)
    public ApiResponse<CategoryResponse> getAllSubCategoryByParentId(
            @PathVariable("shopId") UUID shopId, @PathVariable("id") UUID id) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getAllSubCategoryByParentId(id, shopId))
                .build();
    }

    @PatchMapping(Endpoint.CategoryEndpoint.UPDATE_BY_ID)
    public ApiResponse<String> updateCategoryById(
            @PathVariable("id") UUID id, @RequestBody @Validated UpdateCategoryRequest request) {
        return ApiResponse.<String>builder()
                .message(categoryService.updateCategory(id, request))
                .build();
    }
}
