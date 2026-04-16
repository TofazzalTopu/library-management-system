package com.library.management.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "BOOK",
        indexes = {
                @Index(name = "idx_book_isbn_id", columnList = "ISBN, BOOK_ID")
        }
)
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOOK_ID", updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "ISBN", nullable = false, length = 100)
  @EqualsAndHashCode.Include
  private String isbn;

  @Column(name = "TITLE", nullable = false, length = 100)
  @EqualsAndHashCode.Include
  private String title;

  @Column(name = "AUTHOR", nullable = false, length = 100)
  @EqualsAndHashCode.Include
  private String author;

  @Version
  @Column(name = "VERSION")
  private Long version;

}