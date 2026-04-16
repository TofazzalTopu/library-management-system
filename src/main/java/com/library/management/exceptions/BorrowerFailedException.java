package com.library.management.exceptions;

public class BorrowerFailedException extends RuntimeException {

    public BorrowerFailedException(String message) {
        super(message);
    }

    public BorrowerFailedException(String message, Exception inner) {
        super(message, inner);
    }
}
