package com.bhawani.product_api.mapper;

import com.bhawani.product_api.dto.request.ProductCreateRequest;
import com.bhawani.product_api.dto.request.ProductUpdateRequest;
import com.bhawani.product_api.dto.response.ProductResponse;
import com.bhawani.product_api.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Mapper responsible for converting between:
 *
 * Request DTO  <--> Entity <--> Response DTO
 *
 * Keeping mapping logic in one place makes the code
 * cleaner, reusable and easier to maintain.
 */
@Component
public class ProductMapper {

    /**
     * Converts ProductCreateRequest into Product entity.
     */
    public Product toEntity(ProductCreateRequest request) {

        Product product = new Product();

        product.setName(request.name().trim());

        // Store SKU in uppercase for consistency
        product.setSku(
                request.sku()
                        .trim()
                        .toUpperCase(Locale.ROOT)
        );

        product.setDescription(request.description());

        product.setPrice(request.price());

        product.setQuantity(request.quantity());

        product.setCategory(request.category());

        // Every newly created product is active
        product.setActive(true);

        return product;
    }

    /**
     * Updates an existing Product entity.
     *
     * Only editable fields are updated.
     *
     * Primary key and audit fields remain unchanged.
     */
    public void updateEntity(
            Product product,
            ProductUpdateRequest request
    ) {

        product.setName(request.name().trim());

        product.setSku(
                request.sku()
                        .trim()
                        .toUpperCase(Locale.ROOT)
        );

        product.setDescription(request.description());

        product.setPrice(request.price());

        product.setQuantity(request.quantity());

        product.setCategory(request.category());

        product.setActive(request.active());
    }

    /**
     * Converts Product entity into ProductResponse DTO.
     */
    public ProductResponse toResponse(Product product) {

        return new ProductResponse(

                product.getId(),

                product.getName(),

                product.getSku(),

                product.getDescription(),

                product.getPrice(),

                product.getQuantity(),

                product.getCategory(),

                product.getActive(),

                // Audit fields inherited from BaseEntity
                product.getCreatedAt(),

                product.getUpdatedAt()
        );
    }
}