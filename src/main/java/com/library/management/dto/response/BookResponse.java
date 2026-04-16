package com.library.management.dto.response;

import com.library.management.model.Book;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class BookResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String isbn;
  private String title;
  private String author;

  public static BookResponse of(Book book) {
    return BookResponse.builder().id(book.getId())
        .isbn(book.getIsbn())
        .title(book.getTitle())
        .author(book.getAuthor())
        .build();
  }
}
