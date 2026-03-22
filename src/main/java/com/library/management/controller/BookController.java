package com.library.management.controller;

import com.library.management.annotation.APIDocumentation;
import com.library.management.dto.request.BookRequest;
import com.library.management.dto.response.BookResponse;
import com.library.management.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/libraries/v1/books")
public class BookController {

  private final BookService bookService;

  @PostMapping
  @APIDocumentation
  public ResponseEntity<BookResponse> registerBook(@Valid @RequestBody BookRequest bookRequest) throws URISyntaxException {
    log.info("Received request to register a new book: {}", bookRequest);
    BookResponse bookResponse = bookService.registerBook(bookRequest);

    log.debug("Book registered successfully. BookResponse: {}", bookResponse);

    return ResponseEntity.ok(bookResponse);
  }

  @GetMapping
  @APIDocumentation
  public ResponseEntity<List<BookResponse>> listBooks() {
    return ResponseEntity.ok(bookService.getAllBooks());
  }

}
