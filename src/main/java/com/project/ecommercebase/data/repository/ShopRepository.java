package com.project.ecommercebase.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ecommercebase.data.entity.Shop;
import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.enums.Status;

public interface ShopRepository extends JpaRepository<Shop, UUID> {
    Optional<Shop> findByUser(User user);

    Optional<Shop> findByUserAndStatus(User user, Status status);

    @Query("SELECT s FROM Shop s WHERE s.id = :shopId AND s.status = :status ")
    Optional<Shop> findByIdAndStatus(UUID shopId, Status status);
}
