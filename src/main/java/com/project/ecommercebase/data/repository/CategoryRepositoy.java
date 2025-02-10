package com.project.ecommercebase.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ecommercebase.data.entity.Category;
import com.project.ecommercebase.data.entity.Shop;
import com.project.ecommercebase.dto.response.FlatCategoryResponse;
import com.project.ecommercebase.enums.Status;

public interface CategoryRepositoy extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL AND c.shop = :shop")
    List<Category> findAllCategoriesByShop(Shop shop);

    @Query(
            " SELECT new com.project.ecommercebase.dto.response.FlatCategoryResponse(c.id, c.name, c.parentCategory.id, c.visibility)FROM Category c WHERE c.id = :id")
    Optional<FlatCategoryResponse> findCategoryById(UUID id);

    Optional<Category> findByShopAndId(Shop shop, UUID id);

    Optional<Category> findByIdAndShopAndStatus(UUID id, Shop shop, Status status);

    Boolean existsByIdAndStatus(UUID id, Status status);
}
