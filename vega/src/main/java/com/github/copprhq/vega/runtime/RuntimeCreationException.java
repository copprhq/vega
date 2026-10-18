package com.github.copprhq.vega.runtime;

public class RuntimeCreationException extends RuntimeException {
    public RuntimeCreationException(ReflectiveOperationException exception) {
        super(exception);
    }
}
