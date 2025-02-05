package com.project.ecommercebase.service;

import java.util.List;

import com.project.ecommercebase.dto.request.CategoryRequest;
import com.project.ecommercebase.dto.request.SubCategoryRequest;
import com.project.ecommercebase.dto.response.CategoryResponse;

public interface CategoryService {
    String createCategory(CategoryRequest categoryRequest);

    String createSubCategory(SubCategoryRequest subCategoryRequest);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(String categoryId);

    CategoryResponse getSubCategoryByParentId(String parentId);

    List<CategoryResponse> getAllCategoriesByTree();

    void updateCategory(CategoryRequest categoryRequest);
}
