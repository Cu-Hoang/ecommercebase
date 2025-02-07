package com.project.ecommercebase.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommercebase.constant.Endpoint;
import com.project.ecommercebase.dto.response.ApiResponse;
import com.project.ecommercebase.dto.response.ShopResponse;
import com.project.ecommercebase.service.ShopService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoint.ENDPOINT_SHOP)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShopController {
    ShopService shopService;

    @GetMapping()
    public ApiResponse<List<ShopResponse>> getAllUser() {
        return ApiResponse.<List<ShopResponse>>builder()
                .data(shopService.getAllShops())
                .build();
    }
}
