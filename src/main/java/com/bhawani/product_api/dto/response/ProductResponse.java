package com.bhawani.product_api.dto.response;

import com.bhawani.product_api.entity.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Public response returned by Product API endpoints.
 */
public record ProductResponse(
        Long id,
        String name,
        String sku,
        String description,
        BigDecimal price,
        Integer quantity,
        ProductCategory category,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}