package com.project.ecommercebase.mapper;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.project.ecommercebase.data.entity.Category;
import com.project.ecommercebase.dto.response.CategoryResponse;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parent_id", source = "category", qualifiedByName = "mapParentId")
    @Mapping(target = "subCategories", source = "subCategories", qualifiedByName = "mapSubCategories")
    CategoryResponse categoryToCategoryResponse(Category category);

    @Named("mapParentId")
    default UUID getParentId(Category category) {
        return category.getParentCategory() == null
                ? UUID.fromString("00000000-0000-0000-0000-000000000000")
                : category.getParentCategory().getId();
    }

    @Named("mapSubCategories")
    default Set<CategoryResponse> getSubCategories(Set<Category> subCategories) {
        return (subCategories != null && !subCategories.isEmpty())
                ? subCategories.stream().map(this::categoryToCategoryResponse).collect(Collectors.toSet())
                : new LinkedHashSet<>();
    }
}
