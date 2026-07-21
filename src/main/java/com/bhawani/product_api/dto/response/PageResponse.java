package com.bhawani.product_api.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Standard paginated response returned by collection APIs.
 *
 * @param content       records available on the current page
 * @param page          current zero-based page number
 * @param size          configured page size
 * @param totalElements total matching database records
 * @param totalPages    total pages available
 * @param first         whether this is the first page
 * @param last          whether this is the last page
 * @param hasNext       whether another page is available
 * @param hasPrevious   whether a previous page is available
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean hasNext,
        boolean hasPrevious
) {

    /**
     * Converts Spring Data's Page object into our public API response.
     *
     * This prevents the API contract from depending directly
     * on Spring Data's internal Page implementation.
     */
    public static <T> PageResponse<T> from(Page<T> pageResult) {

        return new PageResponse<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
        );
    }
}