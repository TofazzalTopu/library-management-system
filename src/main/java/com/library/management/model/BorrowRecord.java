package com.library.management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "BORROW_RECORD",
        indexes = {
                @Index(name = "idx_borrow_book_active", columnList = "BOOK_ID, ACTIVE")
        }
)
public class BorrowRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "RECORD_ID", updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private Long recordId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOK_ID", nullable = false)
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BORROWER_ID", nullable = false)
  private Borrower borrower;

  @Column(name = "BORROW_MSG", length = 500)
  private String borrowMsg;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active = false;

  @Column(name = "BORROWED_AT", nullable = false)
  private LocalDateTime borrowedAt;

  @Column(name = "RETURNED_AT")
  private LocalDateTime returnedAt;

  @Version
  @Column(name = "VERSION")
  private Long version;


}
