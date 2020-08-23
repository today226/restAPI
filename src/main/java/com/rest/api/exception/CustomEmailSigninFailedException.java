package com.rest.api.exception;

public class CustomEmailSigninFailedException extends RuntimeException {

    public CustomEmailSigninFailedException(String message, Throwable t) {

        super(message, t);
    }

    public CustomEmailSigninFailedException(String message) {

        super(message);
    }

    public CustomEmailSigninFailedException() {

        super();
    }
}
