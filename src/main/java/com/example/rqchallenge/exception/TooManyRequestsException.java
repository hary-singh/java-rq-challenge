package com.example.rqchallenge.exception;

/**
 * Exception thrown when too many requests are made to the API.
 */
public class TooManyRequestsException extends RuntimeException {

    public TooManyRequestsException(String message) {
        super(message);
    }
}
