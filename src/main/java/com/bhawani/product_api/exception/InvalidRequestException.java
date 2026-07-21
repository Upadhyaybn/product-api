package com.bhawani.product_api.exception;

/**
 * Thrown when API query parameters contain unsupported
 * or logically invalid values.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message){
        super(message);
    }
}
