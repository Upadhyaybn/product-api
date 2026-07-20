package com.bhawani.product_api.service;

import com.bhawani.product_api.entity.Product;

import java.util.List;

/**
 * Defines the business operations supported by Product API.
 */
public interface ProductService {

    Product createProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Product updateProduct(Long productId, Product updatedProduct);

    void deleteProduct(Long productId);
}
