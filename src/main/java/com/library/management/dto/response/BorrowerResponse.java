package com.library.management.dto.response;

import com.library.management.model.Borrower;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private String email;

  public static BorrowerResponse of(Borrower borrower) {
    return BorrowerResponse.builder()
        .id(borrower.getId())
        .name(borrower.getName())
        .email(borrower.getEmail())
        .build();
  }

}