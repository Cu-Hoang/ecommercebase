package com.project.ecommercebase.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateProductRequest(
        String description,
        @Pattern(regexp = ".*\\S.*", message = "must not be empty or just have white space.") String name,
        @PositiveOrZero BigDecimal price,
        @PositiveOrZero Integer quantity,
        String image) {}
