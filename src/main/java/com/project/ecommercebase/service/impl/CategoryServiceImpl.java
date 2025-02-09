package com.project.ecommercebase.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.data.entity.Category;
import com.project.ecommercebase.data.entity.Shop;
import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.data.repository.CategoryRepositoy;
import com.project.ecommercebase.data.repository.ShopRepository;
import com.project.ecommercebase.data.repository.UserRepository;
import com.project.ecommercebase.dto.request.CategoryRequest;
import com.project.ecommercebase.dto.request.UpdateCategoryRequest;
import com.project.ecommercebase.dto.response.CategoryResponse;
import com.project.ecommercebase.dto.response.FlatCategoryResponse;
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

    UserRepository userRepository;

    CategoryMapper categoryMapper;

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String createCategory(CategoryRequest categoryRequest) {
        JwtAuthenticationToken authToken =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UUID vendorId = UUID.fromString(authToken.getName());
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        if (categoryRequest.parent_id() != null) {
            Category category = categoryRepositoy
                    .findById(UUID.fromString(categoryRequest.parent_id()))
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));

            categoryRepositoy.save(Category.builder()
                    .name(categoryRequest.name())
                    .parentCategory(category)
                    .shop(shop)
                    .build());
        } else
            categoryRepositoy.save(
                    Category.builder().name(categoryRequest.name()).shop(shop).build());
        return "Created category successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public List<CategoryResponse> getAllCategories() {
        JwtAuthenticationToken authToken =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UUID vendorId = UUID.fromString(authToken.getName());
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        return categoryRepositoy.findAllCategoriesByShop(shop).stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();
    }

    @Override
    public List<CategoryResponse> getAllCategories(UUID shopId) {
        Shop shop = shopRepository
                .findByIdAndStatus(shopId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        return categoryRepositoy.findAllCategoriesByShop(shop).stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public FlatCategoryResponse getCategoryById(UUID categoryId) {
        FlatCategoryResponse category = categoryRepositoy
                .findCategoryById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
        return category;
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public CategoryResponse getAllSubCategoryByParentId(UUID parentId) {
        JwtAuthenticationToken authToken =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UUID vendorId = UUID.fromString(authToken.getName());
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Category category = categoryRepositoy
                .findByShopAndId(shop, parentId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
        return categoryMapper.categoryToCategoryResponse(category);
    }

    @Override
    public CategoryResponse getAllSubCategoryByParentId(UUID parentId, UUID shopId) {
        Shop shop = shopRepository
                .findByIdAndStatus(shopId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Category category = categoryRepositoy
                .findByShopAndId(shop, parentId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
        return categoryMapper.categoryToCategoryResponse(category);
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String updateCategory(UUID categoryId, UpdateCategoryRequest request) {
        JwtAuthenticationToken authToken =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UUID vendorId = UUID.fromString(authToken.getName());
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Category category = categoryRepositoy
                .findByShopAndId(shop, categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
        category.setName(request.name());
        categoryRepositoy.save(category);
        return "Updated category successfully";
    }
}
