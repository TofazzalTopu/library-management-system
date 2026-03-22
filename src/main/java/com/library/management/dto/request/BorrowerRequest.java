package com.library.management.dto.request;

import com.library.management.model.Borrower;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerRequest {

  @NotBlank(message = "Name must not be blank")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  private String name;

  @NotBlank(message = "Email must not be blank")
  @Email(message = "Email format is invalid")
  @Size(min = 5, max = 50, message = "Email must not exceed 50 characters")
  private String email;

  public Borrower toEntity() {
    Borrower borrower = new Borrower();
    borrower.setName(this.name);
    borrower.setEmail(this.email.toLowerCase(Locale.ROOT));
    return borrower;
  }

}