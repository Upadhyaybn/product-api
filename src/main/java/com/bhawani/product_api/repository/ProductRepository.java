package com.bhawani.product_api.repository;

import com.bhawani.product_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository responsible for Product database operations.
 *
 * JpaRepository provides CRUD, paging and sorting support.
 *
 * JpaSpecificationExecutor allows dynamic filtering using
 * JPA Criteria specifications.
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Finds a product using its unique SKU.
     */
    Optional<Product> findBySku(String sku);

    /**
     * Checks whether the given SKU already exists.
     */
    boolean existsBySku(String sku);

    /**
     * Checks whether another product already uses the SKU.
     *
     * The supplied product ID is excluded from the check.
     */
    boolean existsBySkuAndIdNot(String sku, Long id);
}