package com.project.ecommercebase.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.data.entity.Category;
import com.project.ecommercebase.data.entity.Shop;
import com.project.ecommercebase.data.repository.CategoryRepositoy;
import com.project.ecommercebase.data.repository.ShopRepository;
import com.project.ecommercebase.dto.request.CategoryRequest;
import com.project.ecommercebase.dto.request.SubCategoryRequest;
import com.project.ecommercebase.dto.response.CategoryResponse;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.enums.Status;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.mapper.CategoryMapper;
import com.project.ecommercebase.service.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    ShopRepository shopRepository;

    CategoryRepositoy categoryRepositoy;

    CategoryMapper categoryMapper;

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String createCategory(CategoryRequest categoryRequest) {
        Shop shop = shopRepository
                .findByIdAndStatus(UUID.fromString(categoryRequest.shopId()), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Category category =
                Category.builder().name(categoryRequest.name()).shop(shop).build();

        categoryRepositoy.save(category);
        return "Category created successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String createSubCategory(SubCategoryRequest subCategoryRequest) {
        UUID shopId = UUID.fromString(subCategoryRequest.shopId());
        Shop shop = shopRepository
                .findByIdAndStatus(shopId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        if (!categoryRepositoy.existsById(UUID.fromString(subCategoryRequest.parent_id())))
            throw new AppException(ErrorCode.NOT_EXISTED_CATEGORY);
        Category category = Category.builder()
                .name(subCategoryRequest.name())
                .parent_id(UUID.fromString(subCategoryRequest.parent_id()))
                .shop(shop)
                .build();

        categoryRepositoy.save(category);
        return "Category created successfully";
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepositoy.findAll().stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(String categoryId) {
        return null;
    }

    @Override
    public CategoryResponse getSubCategoryByParentId(String parentId) {
        return null;
    }

    @Override
    public List<CategoryResponse> getAllCategoriesByTree() {
        return categoryRepositoy.findALLByTree().stream().toList();
    }

    @Override
    public void updateCategory(CategoryRequest categoryRequest) {}
}
