package com.bhawani.product_api.exception;


/**
 * Thrown when a requested product does not exist.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long productId) {
        super("Product was not found with id: " + productId);
    }
}
