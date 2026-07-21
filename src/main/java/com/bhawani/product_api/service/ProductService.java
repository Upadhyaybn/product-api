package com.bhawani.product_api.service;

import com.bhawani.product_api.dto.request.ProductCreateRequest;
import com.bhawani.product_api.dto.request.ProductUpdateRequest;
import com.bhawani.product_api.dto.response.PageResponse;
import com.bhawani.product_api.dto.response.ProductResponse;
import com.bhawani.product_api.entity.ProductCategory;

import java.util.List;

/**
 * Defines the business operations supported by Product API.
 */
public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    PageResponse<ProductResponse> getProducts(
            int page,
            int size,
            String sortBy,
            String direction,
            String search,
            ProductCategory category,
            Boolean active
    );

    ProductResponse getProductById(Long productId);

    ProductResponse updateProduct(
            Long productId,
            ProductUpdateRequest request
    );

    void deleteProduct(Long productId);
}
