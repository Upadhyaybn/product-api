package com.bhawani.product_api.specification;

import com.bhawani.product_api.entity.Product;
import com.bhawani.product_api.entity.ProductCategory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Builds dynamic database filtering conditions for Product queries.
 */
public final class ProductSpecification {

    /**
     * Private constructor prevents object creation because
     * this class contains only static utility methods.
     */
    private ProductSpecification() {
    }

    /**
     * Creates one dynamic Product specification from optional filters.
     *
     * Search checks:
     * - Product name
     * - SKU
     * - Description
     *
     * Additional exact filters:
     * - Category
     * - Active status
     */
    public static Specification<Product> withFilters(
            String search,
            ProductCategory category,
            Boolean active
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            /*
             * Always exclude soft-deleted products.
             */
            predicates.add(
                    criteriaBuilder.isFalse(
                            root.get("deleted")
                    )
            );

            /*
             * Optional text search.
             */
            if (StringUtils.hasText(search)) {

                String searchPattern =
                        "%" + search.trim()
                                .toLowerCase(Locale.ROOT) + "%";

                Predicate nameMatches = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        searchPattern
                );

                Predicate skuMatches = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("sku")),
                        searchPattern
                );

                Predicate descriptionMatches = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        searchPattern
                );

                /*
                 * A product matches when the search text is found
                 * in name OR SKU OR description.
                 */
                predicates.add(
                        criteriaBuilder.or(
                                nameMatches,
                                skuMatches,
                                descriptionMatches
                        )
                );
            }

            /*
             * Add category filtering only when category is supplied.
             */
            if (category != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("category"),
                                category
                        )
                );
            }

            /*
             * Add active-status filtering only when active is supplied.
             */
            if (active != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("active"),
                                active
                        )
                );
            }

            /*
             * All supplied filters must match.
             *
             * Example:
             * search=samsung AND category=ELECTRONICS AND active=true
             */
            return criteriaBuilder.and(
                    predicates.toArray(Predicate[]::new)
            );
        };
    }
}
