package com.project.ecommercebase.service;

import java.util.List;
import java.util.UUID;

import com.project.ecommercebase.dto.request.CategoryRequest;
import com.project.ecommercebase.dto.request.UpdateCategoryRequest;
import com.project.ecommercebase.dto.response.CategoryResponse;
import com.project.ecommercebase.dto.response.FlatCategoryResponse;

public interface CategoryService {
    String createCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getAllCategories(UUID shopId);

    FlatCategoryResponse getCategoryById(UUID categoryId);

    CategoryResponse getAllSubCategoryByParentId(UUID parentId);

    CategoryResponse getAllSubCategoryByParentId(UUID parentId, UUID shopId);

    String updateCategory(UUID categoryId, UpdateCategoryRequest request);
}
