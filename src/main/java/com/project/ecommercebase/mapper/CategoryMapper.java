package com.project.ecommercebase.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.project.ecommercebase.data.entity.Category;
import com.project.ecommercebase.dto.response.CategoryResponse;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parent_id", qualifiedByName = "mapParentId")
    CategoryResponse categoryToCategoryResponse(Category category);

    @Named("mapParentId")
    default UUID getParentId(UUID parent_id) {
        return parent_id == null ? UUID.fromString("00000000-0000-0000-0000-000000000000") : parent_id;
    }
}
