package com.bhawani.product_api.service;

import com.bhawani.product_api.dto.request.ProductCreateRequest;
import com.bhawani.product_api.dto.request.ProductUpdateRequest;
import com.bhawani.product_api.dto.response.ProductResponse;
import com.bhawani.product_api.entity.Product;
import com.bhawani.product_api.exception.ProductNotFoundException;
import com.bhawani.product_api.mapper.ProductMapper;
import com.bhawani.product_api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contains the business logic for product operations.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {

        Product product = productMapper.toEntity(request);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {

        Product product = findProduct(productId);

        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse updateProduct(
            Long productId,
            ProductUpdateRequest request
    ) {
        Product existingProduct = findProduct(productId);

        productMapper.updateEntity(existingProduct, request);

        /*
         * Explicit save makes the operation clear.
         * Because the entity is managed, JPA dirty checking
         * would also persist these changes at transaction commit.
         */
        Product updatedProduct =
                productRepository.save(existingProduct);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {

        Product product = findProduct(productId);

        productRepository.delete(product);
    }

    /**
     * Shared internal method for locating a product.
     */
    private Product findProduct(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(
                        () -> new ProductNotFoundException(productId)
                );
    }

}
