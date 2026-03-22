package com.library.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BorrowRequest {

  @NotNull
  private Long borrowerId;

  @NotNull
  private Long bookId;

  @NotNull
  private String borrowMsg;
}