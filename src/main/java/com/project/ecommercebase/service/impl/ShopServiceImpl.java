package com.project.ecommercebase.service.impl;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.data.repository.ShopRepository;
import com.project.ecommercebase.dto.response.ShopResponse;
import com.project.ecommercebase.mapper.ShopMapper;
import com.project.ecommercebase.service.ShopService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ShopServiceImpl implements ShopService {
    ShopRepository shopRepository;

    ShopMapper shopMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<ShopResponse> getAllShops() {
        return shopRepository.findAll().stream()
                .map(shopMapper::shopToShopResponse)
                .toList();
    }
}
