package com.project.ecommercebase.dto.response;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponse(
        UUID id, String name, UUID parent_id, String visibility, Set<CategoryResponse> subCategories) {}
