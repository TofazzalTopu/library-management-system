package com.library.management.controller;

import com.library.management.annotation.APIDocumentation;
import com.library.management.constants.Constants;
import com.library.management.dto.request.BookRequest;
import com.library.management.dto.response.BookResponse;
import com.library.management.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.BOOKS_API_ENDPOINT)
public class BookController {

  private final BookService bookService;

  @PostMapping
  @APIDocumentation
  public ResponseEntity<BookResponse> registerBook(@Valid @RequestBody BookRequest bookRequest) {
    log.info("Received request to register a new book: {}", bookRequest);
    BookResponse bookResponse = bookService.registerBook(bookRequest);

    log.debug("Book registered successfully. BookResponse: {}", bookResponse);

    return ResponseEntity.ok(bookResponse);
  }

  @GetMapping
  @APIDocumentation
  public ResponseEntity<Page<BookResponse>> listBooks(Pageable pageable) {
    return ResponseEntity.ok(bookService.getAllBooks(pageable));
  }

}
