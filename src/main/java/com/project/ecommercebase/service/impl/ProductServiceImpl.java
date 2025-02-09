package com.project.ecommercebase.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.configuration.AuthId;
import com.project.ecommercebase.data.entity.Category;
import com.project.ecommercebase.data.entity.Product;
import com.project.ecommercebase.data.entity.Shop;
import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.data.repository.CategoryRepositoy;
import com.project.ecommercebase.data.repository.ProductRepository;
import com.project.ecommercebase.data.repository.ShopRepository;
import com.project.ecommercebase.data.repository.UserRepository;
import com.project.ecommercebase.dto.request.ProductRequest;
import com.project.ecommercebase.dto.request.UpdateProductRequest;
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
}
