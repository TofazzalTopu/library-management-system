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
        name = "BORROWER",
        indexes = {
                @Index(name = "idx_borrower_email", columnList = "EMAIL")
        }
)
public class Borrower {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BORROWER_ID", updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @Column(name = "EMAIL", unique = true, nullable = false, length = 50)
  private String email;

  @Version
  @Column(name = "VERSION")
  private Long version;

}