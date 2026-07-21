package com.bhawani.product_api.dto.request;
import com.bhawani.product_api.entity.ProductCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request model used for complete PUT updates.
 */
public record ProductUpdateRequest(

        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 150)
        String name,

        @NotBlank(message = "SKU is required")
        @Size(min = 3, max = 50)
        String sku,

        @Size(max = 1000)
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(
                value = "0.01",
                message = "Price must be greater than zero"
        )
        BigDecimal price,

        @NotNull(message = "Quantity is required")
        @Min(
                value = 0,
                message = "Quantity cannot be negative"
        )
        Integer quantity,

        @NotNull(message = "Category is required")
        ProductCategory category,

        @NotNull(message = "Active status is required")
        Boolean active
) {
}