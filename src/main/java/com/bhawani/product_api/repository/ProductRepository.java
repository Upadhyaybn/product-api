package com.bhawani.product_api.repository;

import com.bhawani.product_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository responsible for Product database operations.
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Finds a non-deleted product by ID.
     */
    Optional<Product> findByIdAndDeletedFalse(Long id);

    /**
     * Finds a non-deleted product by SKU.
     */
    Optional<Product> findBySkuAndDeletedFalse(String sku);

    /**
     * Checks whether a non-deleted product already uses the SKU.
     */
    boolean existsBySkuAndDeletedFalse(String sku);

    /**
     * Checks whether another non-deleted product uses the SKU.
     *
     * The current product ID is excluded during an update.
     */
    boolean existsBySkuAndIdNotAndDeletedFalse(
            String sku,
            Long id
    );
}