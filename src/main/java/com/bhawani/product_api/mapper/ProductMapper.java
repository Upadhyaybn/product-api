package com.bhawani.product_api.mapper;
import com.bhawani.product_api.dto.request.ProductCreateRequest;
import com.bhawani.product_api.dto.request.ProductUpdateRequest;
import com.bhawani.product_api.dto.response.ProductResponse;
import com.bhawani.product_api.entity.Product;
import org.springframework.stereotype.Component;

/**
 * Converts between API DTOs and the Product entity.
 */
@Component
public class ProductMapper {

    /**
     * Converts a create request into a new Product entity.
     */
    public Product toEntity(ProductCreateRequest request) {

        Product product = new Product();

        product.setName(request.name());
        product.setSku(normalizeSku(request.sku()));
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setCategory(request.category());
        product.setActive(true);

        return product;
    }

    /**
     * Copies update-request values into an existing entity.
     *
     * We update the existing managed entity so its ID and
     * auditing values remain unchanged.
     */
    public void updateEntity(
            Product product,
            ProductUpdateRequest request
    ) {
        product.setName(request.name());
        product.setSku(normalizeSku(request.sku()));
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setCategory(request.category());
        product.setActive(request.active());
    }

    /**
     * Converts a Product entity into its public API response.
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
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    /**
     * Keeps SKU values consistent and case-insensitive.
     */
    private String normalizeSku(String sku) {
        return sku.trim().toUpperCase();
    }
}