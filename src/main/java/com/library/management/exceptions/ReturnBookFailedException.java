package com.library.management.exceptions;

public class ReturnBookFailedException extends RuntimeException {

    public ReturnBookFailedException(String message) {
        super(message);
    }

    public ReturnBookFailedException(String message, Exception inner) {
        super(message, inner);
    }
}
