package org.springtech.springmarket.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) { super(message); }
}
