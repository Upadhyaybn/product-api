package com.bhawani.product_api.service;

import com.bhawani.product_api.dto.request.ProductCreateRequest;
import com.bhawani.product_api.dto.request.ProductUpdateRequest;
import com.bhawani.product_api.dto.response.PageResponse;
import com.bhawani.product_api.dto.response.ProductResponse;
import com.bhawani.product_api.entity.Product;
import com.bhawani.product_api.entity.ProductCategory;
import com.bhawani.product_api.exception.DuplicateSkuException;
import com.bhawani.product_api.exception.InvalidRequestException;
import com.bhawani.product_api.exception.ProductNotFoundException;
import com.bhawani.product_api.mapper.ProductMapper;
import com.bhawani.product_api.repository.ProductRepository;
import com.bhawani.product_api.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

/**
 * Service implementation containing business logic
 * for Product API operations.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    /**
     * Fields that clients are allowed to use for sorting.
     *
     * This whitelist prevents clients from passing unsupported
     * or internal entity property names.
     */
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "name",
            "sku",
            "price",
            "quantity",
            "category",
            "active",
            "createdAt",
            "updatedAt"
    );

    /**
     * Maximum number of products allowed in one page.
     *
     * This protects the API and database from very large requests.
     */
    private static final int MAX_PAGE_SIZE = 100;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Constructor injection keeps dependencies mandatory,
     * immutable and easy to mock during unit testing.
     */
    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Creates a new product.
     *
     * A duplicate SKU is rejected before attempting
     * to insert the product into the database.
     */
    @Override
    public ProductResponse createProduct(
            ProductCreateRequest request
    ) {
        String normalizedSku = normalizeSku(request.sku());

        /*
         * SKU is a business identifier and must remain unique.
         */
        if (productRepository.existsBySkuAndDeletedFalse(normalizedSku)) {
            throw new DuplicateSkuException(normalizedSku);
        }

        /*
         * ProductMapper converts the request DTO
         * into a Product entity.
         */
        Product product = productMapper.toEntity(request);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    /**
     * Returns paginated products with optional:
     *
     * - Text search
     * - Category filtering
     * - Active-status filtering
     * - Sorting
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProducts(
            int page,
            int size,
            String sortBy,
            String direction,
            String search,
            ProductCategory category,
            Boolean active
    ) {
        /*
         * Validate client-provided paging and sorting parameters
         * before building the database query.
         */
        validatePaginationAndSorting(
                page,
                size,
                sortBy,
                direction
        );

        /*
         * Convert values such as "asc" and "desc"
         * into Spring Data's Sort.Direction enum.
         */
        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        /*
         * Page numbering is zero-based:
         *
         * page=0 → first page
         * page=1 → second page
         */
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        /*
         * Build a dynamic JPA specification using only
         * the filters supplied by the client.
         */
        var productSpecification =
                ProductSpecification.withFilters(
                        search,
                        category,
                        active
                );

        /*
         * Execute a paginated and filtered database query.
         */
        Page<Product> productPage =
                productRepository.findAll(
                        productSpecification,
                        pageable
                );

        /*
         * Convert every Product entity into ProductResponse
         * while preserving the pagination metadata.
         */
        Page<ProductResponse> responsePage =
                productPage.map(productMapper::toResponse);

        return PageResponse.from(responsePage);
    }

    /**
     * Returns one product by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(
            Long productId
    ) {
        Product product = findProductById(productId);

        return productMapper.toResponse(product);
    }

    /**
     * Completely updates an existing product.
     *
     * This endpoint follows PUT semantics, meaning all editable
     * fields should be supplied in ProductUpdateRequest.
     */
    @Override
    public ProductResponse updateProduct(
            Long productId,
            ProductUpdateRequest request
    ) {
        /*
         * First confirm that the product being updated exists.
         */
        Product existingProduct =
                findProductById(productId);

        String normalizedSku =
                normalizeSku(request.sku());

        /*
         * Check whether another product already uses the SKU.
         *
         * The current product ID is excluded so that keeping
         * the same SKU during an update is allowed.
         */
        if (productRepository.existsBySkuAndIdNotAndDeletedFalse(
                normalizedSku,
                productId
        )) {
            throw new DuplicateSkuException(normalizedSku);
        }

        /*
         * Copy request fields into the existing managed entity.
         *
         * ID, createdAt and other database-controlled values
         * remain unchanged.
         */
        productMapper.updateEntity(
                existingProduct,
                request
        );

        /*
         * Because this method is transactional, JPA dirty checking
         * would persist the change at transaction commit.
         *
         * Explicit save is retained to keep the operation clear.
         */
        Product updatedProduct =
                productRepository.save(existingProduct);

        return productMapper.toResponse(updatedProduct);
    }

    /**
     * Deletes an existing product.
     */
    @Override
    public void deleteProduct(Long productId) {

        // Find only an existing, non-deleted product
        Product product = findProductById(productId);

        // Mark the product as deleted
        product.setDeleted(true);

        // Deleted products should no longer be active
        product.setActive(false);

        // Record when the deletion happened
        product.setDeletedAt(LocalDateTime.now());

        /*
         * Save the modified entity.
         *
         * Hibernate generates an UPDATE statement instead of DELETE.
         */
        productRepository.save(product);
    }

    /**
     * Finds only a non-deleted product.
     *
     * A soft-deleted product behaves like a missing resource
     * for normal API operations.
     */
    private Product findProductById(
            Long productId
    ) {
        return productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(
                        () -> new ProductNotFoundException(productId)
                );
    }

    /**
     * Validates paging and sorting query parameters.
     */
    private void validatePaginationAndSorting(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        if (page < 0) {
            throw new InvalidRequestException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new InvalidRequestException(
                    "Page size must be between 1 and "
                            + MAX_PAGE_SIZE
            );
        }

        if (sortBy == null
                || !ALLOWED_SORT_FIELDS.contains(sortBy)) {

            throw new InvalidRequestException(
                    "Unsupported sort field: "
                            + sortBy
                            + ". Allowed fields are: "
                            + ALLOWED_SORT_FIELDS
            );
        }

        if (direction == null
                || (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc"))) {

            throw new InvalidRequestException(
                    "Sort direction must be either 'asc' or 'desc'"
            );
        }
    }

    /**
     * Normalizes SKU values for consistent comparison and storage.
     *
     * Example:
     *
     * " samsung-s25-001 "
     * becomes
     * "SAMSUNG-S25-001"
     */
    private String normalizeSku(
            String sku
    ) {
        return sku.trim()
                .toUpperCase(Locale.ROOT);
    }
}
