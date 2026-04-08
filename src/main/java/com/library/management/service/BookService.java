package com.library.management.service;

import com.library.management.dto.request.BookRequest;
import com.library.management.dto.response.BookResponse;
import com.library.management.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

  BookResponse registerBook(BookRequest bookRequest);

  Page<BookResponse> getAllBooks(Pageable pageable);

  Book findById(Long id);

  void validateBookISBNConsistency(Book existingBook, BookRequest request);
}
