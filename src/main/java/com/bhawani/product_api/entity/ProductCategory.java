package com.bhawani.product_api.entity;

import jakarta.persistence.Entity;

/**
 * Defines the supported product categories.
 *
 * Enum is used to restrict product categories
 * to a fixed set of valid values.
 */

public enum ProductCategory {

    ELECTRONICS,
    FASHION,
    HOME,
    BOOKS,
    SPORTS,
    OTHER
}