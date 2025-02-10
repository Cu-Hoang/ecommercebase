package com.project.ecommercebase.data.repository;

import java.util.Optional;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommercebase.data.entity.*;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, UUID> {
    Optional<ProductAttributeValue> findByProductAndAttributeAndValue(
            Product product, Attribute attribute, Value value);

    Boolean existsByAttribute(Attribute attribute);

    Boolean existsByValue(Value value);
}
