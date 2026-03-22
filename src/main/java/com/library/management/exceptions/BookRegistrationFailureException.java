package com.library.management.exceptions;

public class BookRegistrationFailureException extends RuntimeException {

    public BookRegistrationFailureException(String message) {
        super(message);
    }

    public BookRegistrationFailureException(String message, Exception inner) {
        super(message, inner);
    }
}
