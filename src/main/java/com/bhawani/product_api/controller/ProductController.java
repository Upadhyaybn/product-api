package com.bhawani.product_api.controller;

import com.bhawani.product_api.entity.Product;
import com.bhawani.product_api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Exposes REST endpoints for Product operations.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructor injection provides ProductService
     * to the controller.
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product.
     *
     * Endpoint:
     * POST /api/v1/products
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product
    ) {

        Product createdProduct =
                productService.createProduct(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    /**
     * Returns all products.
     *
     * Endpoint:
     * GET /api/v1/products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        List<Product> products =
                productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    /**
     * Returns one product by ID.
     *
     * Endpoint:
     * GET /api/v1/products/{id}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long productId
    ) {

        Product product =
                productService.getProductById(productId);

        return ResponseEntity.ok(product);
    }

    /**
     * Completely updates an existing product.
     *
     * Endpoint:
     * PUT /api/v1/products/{id}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product
    ) {

        Product updatedProduct =
                productService.updateProduct(productId, product);

        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Deletes a product.
     *
     * Endpoint:
     * DELETE /api/v1/products/{id}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ) {

        productService.deleteProduct(productId);

        /*
         * HTTP 204 means the operation succeeded
         * and there is no response body.
         */
        return ResponseEntity.noContent().build();
    }

}
