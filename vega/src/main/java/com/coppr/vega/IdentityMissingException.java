package com.coppr.vega;

public class IdentityMissingException extends RuntimeException {

    public IdentityMissingException(Object entity) {
        super("identity for entity " + entity.getClass().getName() + " is missing");
    }
}
