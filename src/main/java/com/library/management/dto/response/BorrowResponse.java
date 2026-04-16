package com.library.management.dto.response;

import com.library.management.model.BorrowRecord;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BorrowResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long recordId;
  private Long borrowerId;
  private Long bookId;
  private String message;
  private boolean hasBorrowed;

  public static BorrowResponse of(BorrowRecord borrowRecord) {
    return BorrowResponse.builder()
        .recordId(borrowRecord.getRecordId())
        .borrowerId(borrowRecord.getBorrower().getId())
        .bookId(borrowRecord.getBook().getId())
        .hasBorrowed(borrowRecord.isActive())
        .message(borrowRecord.getBorrowMsg())
        .build();
  }
}