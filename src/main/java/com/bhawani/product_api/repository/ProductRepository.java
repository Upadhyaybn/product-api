package com.bhawani.product_api.repository;

import com.bhawani.product_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository provides database operations for Product.
 *
 * JpaRepository already provides:
 * save()
 * findById()
 * findAll()
 * deleteById()
 * existsById()
 * count()
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a product using its unique SKU.
     *
     * Spring Data JPA automatically generates the query
     * based on the method name.
     */
    Optional<Product> findBySku(String sku);

    /**
     * Checks whether a product already exists with the SKU.
     */
    boolean existsBySku(String sku);
}
