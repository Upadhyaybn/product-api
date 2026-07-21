package com.bhawani.product_api.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response returned by every API endpoint.
 */
public record ApiErrorResponse(

        // Time when the error occurred
        LocalDateTime timestamp,

        // HTTP status code, such as 400, 404 or 409
        int status,

        // Standard HTTP error name
        String error,

        // Human-readable error explanation
        String message,

        // Requested API endpoint
        String path,

        // Field-level validation errors, when applicable
        Map<String, String> validationErrors
) {
}

