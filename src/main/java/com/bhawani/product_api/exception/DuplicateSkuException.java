package com.bhawani.product_api.exception;


/**
 * Thrown when a product is created or updated
 * using an SKU that already exists.
 */
public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String sku) {
        super("Product already exists with SKU: " + sku);
    }
}
