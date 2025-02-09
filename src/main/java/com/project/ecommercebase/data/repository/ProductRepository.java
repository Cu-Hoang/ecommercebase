package com.project.ecommercebase.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.Category;
import com.project.ecommercebase.data.entity.Product;
import com.project.ecommercebase.data.entity.Shop;
import com.project.ecommercebase.enums.Status;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByIdAndShopAndStatus(UUID id, Shop shop, Status status);

    List<Product> findByShopAndStatus(Shop shop, Status status);

    Optional<Product> findByIdAndStatus(UUID id, Status status);

    List<Product> findByCategoryAndShopAndStatus(Category category, Shop shop, Status status);
}
