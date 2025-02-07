package com.project.ecommercebase.dto.response;

import java.util.UUID;

import com.project.ecommercebase.enums.Visibility;

public record FlatCategoryResponse(UUID id, String name, UUID parent_id, Visibility visibility) {
    public FlatCategoryResponse(UUID id, String name, UUID parent_id, Visibility visibility) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id == null ? UUID.fromString("00000000-0000-0000-0000-000000000000") : parent_id;
        this.visibility = visibility;
    }
}
