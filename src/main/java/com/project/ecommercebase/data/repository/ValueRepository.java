package com.project.ecommercebase.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.Attribute;
import com.project.ecommercebase.data.entity.Value;

public interface ValueRepository extends JpaRepository<Value, UUID> {
    Boolean existsByAttribute(Attribute attribute);

    Optional<Value> findByIdAndAttribute(UUID id, Attribute attribute);
}
