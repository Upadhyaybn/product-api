package com.bhawani.product_api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product is a JPA entity representing the products table.
 */
@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_sku",
                        columnNames = "sku"
                )
        }
)
public class Product extends BaseEntity {

    /**
     * Primary key of the products table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Public product name.
     */
    @Column(nullable = false, length = 150)
    private String name;

    /**
     * Unique business identifier for the product.
     *
     * Example:
     * LAPTOP-DELL-001
     */
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    /**
     * Detailed product description.
     */
    @Column(length = 1000)
    private String description;

    /**
     * BigDecimal should be used for monetary values.
     * double should be avoided because it may introduce
     * floating-point precision problems.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    /**
     * Available quantity in inventory.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Product category is stored as readable text.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ProductCategory category;

    /**
     * Indicates whether the product is available.
     */
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * Stores the time when the product was created.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Stores the time when the product was last modified.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * This method runs automatically before the entity
     * is inserted into the database.
     */
    @PrePersist
    public void beforeCreate() {
        LocalDateTime currentTime = LocalDateTime.now();

        this.createdAt = currentTime;
        this.updatedAt = currentTime;

        if (this.active == null) {
            this.active = true;
        }
    }

    /**
     * This method runs automatically before the entity
     * is updated in the database.
     */
    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Product() {
    }

    public Product(
            Long id,
            String name,
            String sku,
            String description,
            BigDecimal price,
            Integer quantity,
            ProductCategory category,
            Boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
