package com.project.ecommercebase.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.request.CategoryRequest;
import com.project.ecommercebase.dto.request.SubCategoryRequest;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.dto.response.CategoryResponse;
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

    @PostMapping(Endpoint.CategoryEndpoint.CREATE_SUBCATEGORY)
    public ApiResponse<String> createSubCategory(@RequestBody @Validated SubCategoryRequest subCategoryRequest) {
        return ApiResponse.<String>builder()
                .message(categoryService.createSubCategory(subCategoryRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories())
                .build();
    }

    @GetMapping(Endpoint.CategoryEndpoint.GET_TREE)
    public ApiResponse<List<CategoryResponse>> getAllCategoriesByTree() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategoriesByTree())
                .build();
    }
}
