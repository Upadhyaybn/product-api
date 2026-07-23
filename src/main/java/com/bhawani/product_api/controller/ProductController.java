package com.bhawani.product_api.controller;

import com.bhawani.product_api.dto.request.ProductCreateRequest;
import com.bhawani.product_api.dto.request.ProductUpdateRequest;
import com.bhawani.product_api.dto.response.ProductResponse;
import com.bhawani.product_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bhawani.product_api.dto.response.PageResponse;
import com.bhawani.product_api.entity.ProductCategory;

import java.net.URI;

/**
 * Exposes REST endpoints for Product operations.
 */
@RestController
@RequestMapping("/api/v1/products")
    public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product.
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        ProductResponse response =
                productService.createProduct(request);

        URI location =
                URI.create("/api/v1/products/" + response.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    /**
     * Returns a paginated and optionally filtered product list.
     *
     * Examples:
     *
     * GET /api/v1/products
     * GET /api/v1/products?page=0&size=5
     * GET /api/v1/products?sortBy=price&direction=desc
     * GET /api/v1/products?search=samsung
     * GET /api/v1/products?category=ELECTRONICS
     * GET /api/v1/products?active=true
     */
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            ProductCategory category,

            @RequestParam(required = false)
            Boolean active
    ) {
        PageResponse<ProductResponse> response =
                productService.getProducts(
                        page,
                        size,
                        sortBy,
                        direction,
                        search,
                        category,
                        active
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Returns one product by its database identifier.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                productService.getProductById(productId)
        );
    }

    /**
     * Completely replaces the editable fields of a product.
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return ResponseEntity.ok(
                productService.updateProduct(productId, request)
        );
    }

    /**
     * Soft deletes a product.
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

}
