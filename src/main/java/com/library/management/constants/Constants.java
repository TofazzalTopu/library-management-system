package com.library.management.constants;

public class Constants {


    private Constants() {
    }
    public static final String CORRELATION_ID = "X-Correlation-Id";

    public static final String API_VERSION_V1 = "v1";
    public static final String API_LIBRARIES = "/api/libraries/";

    public static final String API_ENDPOINT = API_LIBRARIES + API_VERSION_V1;
    public static final String BOOKS_API_ENDPOINT = API_ENDPOINT + "/books";
    public static final String BORROWER_API_ENDPOINT = API_ENDPOINT + "/borrowers";
    public static final String BORROW_API_ENDPOINT = API_ENDPOINT + "/borrows";
    public static final String BOOK_NOT_FOUND = "Book not found";
    public static final String BORROWER_NOT_FOUND = "Borrower not found";
    public static final String BORROWER_EMAIL_ALREADY_EXISTS = "Borrower Email already exists";
    public static final String BOOK_ALREADY_RETURNED = "Selected book already returned";
    public static final String BOOK_IS_ALREADY_BORROWED = "Selected book is already borrowed";
    public static final String BORROW_FAILED_BOOK_IS_ALREADY_BORROWED = "Borrow failed: Book {} is already borrowed";


    //Redis cache names
    public static final String CACHE_NAME_BOOK = "book";
    public static final String CACHE_NAME_BORROWER = "borrower";



}
