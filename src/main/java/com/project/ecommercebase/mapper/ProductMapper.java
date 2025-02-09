package com.project.ecommercebase.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.project.ecommercebase.data.entity.Product;
import com.project.ecommercebase.dto.request.ProductRequest;
import com.project.ecommercebase.dto.request.UpdateProductRequest;
import com.project.ecommercebase.dto.response.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product productRequestToProduct(ProductRequest productRequest);

    ProductResponse productToProductResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product product, UpdateProductRequest updateProductRequest);
}
