package com.project.ecommercebase.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.project.ecommercebase.enums.Visibility;

public record ProductResponse(
        UUID id,
        String description,
        String name,
        BigDecimal price,
        Integer quantity,
        String image,
        Visibility visibility) {
    public ProductResponse(
            UUID id,
            String description,
            String name,
            BigDecimal price,
            Integer quantity,
            String image,
            Visibility visibility) {
        this.id = id;
        this.description = description == null ? "" : description;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image == null ? "" : image;
        this.visibility = visibility;
    }
}
