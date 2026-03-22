package com.library.management.dto.response;

import com.library.management.model.Borrower;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerResponse {

  private Long borrowerId;
  private String name;
  private String email;

  public static BorrowerResponse of(Borrower borrower) {
    return BorrowerResponse.builder()
        .borrowerId(borrower.getId())
        .name(borrower.getName())
        .email(borrower.getEmail())
        .build();
  }

}