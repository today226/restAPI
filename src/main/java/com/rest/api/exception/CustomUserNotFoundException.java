package com.rest.api.exception;

public class CustomUserNotFoundException extends RuntimeException {

    public CustomUserNotFoundException(String message, Throwable t) {

        super(message, t);
    }

    public CustomUserNotFoundException(String message) {

        super(message);
    }

    public CustomUserNotFoundException() {

        super();
    }
}
