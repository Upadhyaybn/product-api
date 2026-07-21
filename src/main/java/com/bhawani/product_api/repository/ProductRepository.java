package com.bhawani.product_api.repository;

import com.bhawani.product_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provides database operations for Product entities.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a product using its unique SKU.
     */
    Optional<Product> findBySku(String sku);

    /**
     * Checks whether a product already exists with the given SKU.
     */
    boolean existsBySku(String sku);

    /**
     * Used during update.
     *
     * It checks whether another product already has the same SKU,
     * excluding the product currently being updated.
     */
    boolean existsBySkuAndIdNot(String sku, Long id);
}