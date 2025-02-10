package com.project.ecommercebase.data.repository;

import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.ecommercebase.data.entity.Attribute;
import com.project.ecommercebase.data.entity.Shop;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    Optional<Attribute> findByIdAndShop(UUID id, Shop shop);

    Boolean existsByIdAndShop(UUID id, Shop shop);

    @Modifying
    @Transactional
    @Query("DELETE FROM Attribute c WHERE c.id = :id")
    void deleteAttributeById(UUID id);
}
