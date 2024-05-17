package org.springtech.springmarket.handler;

public class AgencyNotFoundException extends RuntimeException {
    public AgencyNotFoundException(String message) {
        super(message);
    }
}
