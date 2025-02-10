package com.project.ecommercebase.dto.request;

import org.hibernate.validator.constraints.UUID;

public record ProductAttributeValueRequest(@UUID String productId, @UUID String attributeId, @UUID String valueId) {}
