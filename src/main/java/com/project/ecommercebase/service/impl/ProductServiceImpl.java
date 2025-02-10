package com.project.ecommercebase.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.configuration.AuthId;
import com.project.ecommercebase.data.entity.*;
import com.project.ecommercebase.data.repository.*;
import com.project.ecommercebase.dto.request.*;
import com.project.ecommercebase.dto.response.ProductResponse;
import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.enums.Status;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.mapper.ProductMapper;
import com.project.ecommercebase.service.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    UserRepository userRepository;

    ShopRepository shopRepository;

    CategoryRepositoy categoryRepositoy;

    AttributeRepository attributeRepository;

    ValueRepository valueRepository;

    ProductAttributeValueRepository productAttributeValueRepository;

    ProductMapper productMapper;

    AuthId authId;

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String createProduct(ProductRequest productRequest, UUID categoryId) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Category category = categoryRepositoy
                .findByIdAndShopAndStatus(categoryId, shop, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty())
            throw new AppException(ErrorCode.LOWEST_CATEGORY);
        Product product = productMapper.productRequestToProduct(productRequest);
        product.setCategory(category);
        product.setShop(shop);
        productRepository.save(product);
        return "Created product successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String updateProduct(UpdateProductRequest updateProductRequest, UUID productId) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Product product = productRepository
                .findByIdAndShopAndStatus(productId, shop, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_PRODUCT));
        if (!categoryRepositoy.existsByIdAndStatus(product.getCategory().getId(), Status.ACTIVE))
            throw new AppException(ErrorCode.NOT_EXISTED_CATEGORY);
        productMapper.updateProduct(product, updateProductRequest);
        productRepository.save(product);
        return "Update product successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public List<ProductResponse> getAllProducts() {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        return productRepository.findByShopAndStatus(shop, Status.ACTIVE).stream()
                .map(productMapper::productToProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getAllProducts(UUID shopId) {
        Shop shop = shopRepository
                .findByIdAndStatus(shopId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        return productRepository.findByShopAndStatus(shop, Status.ACTIVE).stream()
                .map(productMapper::productToProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProduct(UUID id) {
        Product product = productRepository
                .findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_PRODUCT));
        if (Boolean.FALSE.equals(
                categoryRepositoy.existsByIdAndStatus(product.getCategory().getId(), Status.ACTIVE)))
            throw new AppException(ErrorCode.NOT_EXISTED_CATEGORY);
        if (Boolean.FALSE.equals(
                shopRepository.existsByIdAndStatus(product.getShop().getId(), Status.ACTIVE)))
            throw new AppException(ErrorCode.NOT_EXISTED_SHOP);
        return productMapper.productToProductResponse(product);
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public List<ProductResponse> getAllProductsByCategory(UUID categoryId) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Category category = categoryRepositoy
                .findByIdAndShopAndStatus(categoryId, shop, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty())
            throw new AppException(ErrorCode.LOWEST_CATEGORY);
        return productRepository.findByCategoryAndShopAndStatus(category, shop, Status.ACTIVE).stream()
                .map(productMapper::productToProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getAllProductsByCategory(UUID categoryId, UUID shopId) {
        Shop shop = shopRepository
                .findByIdAndStatus(shopId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Category category = categoryRepositoy
                .findByIdAndShopAndStatus(categoryId, shop, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty())
            throw new AppException(ErrorCode.LOWEST_CATEGORY);
        return productRepository.findByCategoryAndShopAndStatus(category, shop, Status.ACTIVE).stream()
                .map(productMapper::productToProductResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String createAttribute(AttributeRequest attributeRequest) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Attribute attribute =
                Attribute.builder().name(attributeRequest.name()).shop(shop).build();
        attributeRepository.save(attribute);
        return "Created attribute successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String createAttributeValue(UUID attributeId, ValueRequest valueRequest) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Attribute attribute = attributeRepository
                .findByIdAndShop(attributeId, shop)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ATTRIBUTE));
        Value value =
                Value.builder().value(valueRequest.value()).attribute(attribute).build();
        valueRepository.save(value);
        return "Created value successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String updateAttribute(UUID id, AttributeRequest attributeRequest) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Attribute attribute = attributeRepository
                .findByIdAndShop(id, shop)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ATTRIBUTE));
        attribute.setName(attributeRequest.name());
        attributeRepository.save(attribute);
        return "Updated attribute successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String updateAttributeValue(UUID id, ValueRequest valueRequest) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Value value = valueRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_VALUE));
        if (!attributeRepository.existsByIdAndShop(value.getAttribute().getId(), shop))
            throw new AppException(ErrorCode.NOT_EXISTED_ATTRIBUTE);
        value.setValue(valueRequest.value());
        valueRepository.save(value);
        return "Updated value successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String deleteAttribute(UUID id) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Attribute attribute = attributeRepository
                .findByIdAndShop(id, shop)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ATTRIBUTE));
        if (valueRepository.existsByAttribute(attribute)) throw new AppException(ErrorCode.EXISTED_VALUE_ATTRIBUTE);
        if (productAttributeValueRepository.existsByAttribute(attribute))
            throw new AppException(ErrorCode.EXISTED_VALUE_PRODUCT);
        attributeRepository.deleteAttributeById(attribute.getId());
        return "Deleted attribute successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String deleteAttributeValue(UUID id) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Value value = valueRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_VALUE));
        if (!attributeRepository.existsByIdAndShop(value.getAttribute().getId(), shop))
            throw new AppException(ErrorCode.NOT_EXISTED_ATTRIBUTE);
        if (productAttributeValueRepository.existsByValue(value))
            throw new AppException(ErrorCode.EXISTED_VALUE_PRODUCT);
        valueRepository.delete(value);
        return "Deleted value successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String addAttributeValueToProduct(ProductAttributeValueRequest request) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Product product = productRepository
                .findByIdAndShopAndStatus(UUID.fromString(request.productId()), shop, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_PRODUCT));
        Attribute attribute = attributeRepository
                .findByIdAndShop(UUID.fromString(request.attributeId()), shop)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ATTRIBUTE));
        Value value = valueRepository
                .findByIdAndAttribute(UUID.fromString(request.valueId()), attribute)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_VALUE));
        ProductAttributeValue productAttributeValue = ProductAttributeValue.builder()
                .product(product)
                .attribute(attribute)
                .value(value)
                .build();
        productAttributeValueRepository.save(productAttributeValue);
        return "Added product attribute value successfully";
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public String deleteAttributeValueFromProduct(ProductAttributeValueRequest request) {
        UUID vendorId = authId.getId();
        User vendor = userRepository
                .findByIdAndStatus(vendorId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));
        Shop shop = shopRepository
                .findByUserAndStatus(vendor, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_SHOP));
        Product product = productRepository
                .findByIdAndShopAndStatus(UUID.fromString(request.productId()), shop, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_PRODUCT));
        Attribute attribute = attributeRepository
                .findByIdAndShop(UUID.fromString(request.attributeId()), shop)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ATTRIBUTE));
        Value value = valueRepository
                .findByIdAndAttribute(UUID.fromString(request.valueId()), attribute)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_VALUE));
        ProductAttributeValue productAttributeValue = productAttributeValueRepository
                .findByProductAndAttributeAndValue(product, attribute, value)
                .orElseThrow(() -> new AppException(ErrorCode.UNCLASSIFIED_EXCEPTION));
        productAttributeValueRepository.delete(productAttributeValue);
        return "Deleted product attribute value successfully";
    }
}
