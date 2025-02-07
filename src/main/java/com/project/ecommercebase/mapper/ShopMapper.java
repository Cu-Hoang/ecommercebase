package com.project.ecommercebase.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.project.ecommercebase.data.entity.Shop;
import com.project.ecommercebase.dto.response.ShopResponse;
import com.project.ecommercebase.enums.ShopTag;

@Mapper(componentModel = "spring")
public interface ShopMapper {
    @Mapping(target = "shopTag", defaultExpression = "java(getShopTag(shop.getShopTag()))")
    ShopResponse shopToShopResponse(Shop shop);

    @Named("mapShopTag")
    default String getShopTag(ShopTag shopTag) {
        return shopTag == null ? "" : shopTag.name();
    }
}
