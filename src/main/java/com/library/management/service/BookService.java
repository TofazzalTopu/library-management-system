package com.library.management.service;

import com.library.management.dto.request.BookRequest;
import com.library.management.dto.response.BookResponse;
import com.library.management.model.Book;

import java.util.List;

public interface BookService {

  BookResponse registerBook(BookRequest bookRequest);

  List<BookResponse> getAllBooks();

  Book findById(Long id);

  void validateBookISBNConsistency(Book existingBook, BookRequest request);
}
