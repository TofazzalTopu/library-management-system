package com.library.management.dto.response;

import com.library.management.model.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookResponse {

  private Long bookId;
  private String isbn;
  private String title;
  private String author;

  public static BookResponse of(Book book) {
    return BookResponse.builder().bookId(book.getId())
        .isbn(book.getIsbn())
        .title(book.getTitle())
        .author(book.getAuthor())
        .build();
  }
}
