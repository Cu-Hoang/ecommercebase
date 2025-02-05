package com.project.ecommercebase.dto.response;

import java.util.UUID;

public record ShopResponse(UUID id, String name, String province, String shopTag, String status) {}
