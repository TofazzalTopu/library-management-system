package com.library.management.dto.response;

import com.library.management.model.BorrowRecord;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class BorrowRecordResponse implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private Long bookId;
  private String bookTitle;
  private String isbn;
  private Long borrowerId;
  private String borrowerName;
  private LocalDateTime borrowedAt;

  public static BorrowRecordResponse of(BorrowRecord borrowRecord) {
    return BorrowRecordResponse.builder()
        .id(borrowRecord.getRecordId())
        .bookId(borrowRecord.getBook().getId())
        .bookTitle(borrowRecord.getBook().getTitle())
        .isbn(borrowRecord.getBook().getIsbn())
        .borrowerId(borrowRecord.getBorrower().getId())
        .borrowerName(borrowRecord.getBorrower().getName())
        .borrowedAt(borrowRecord.getBorrowedAt())
        .build();
  }
}
