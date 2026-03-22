package com.library.management.controller;

import com.library.management.dto.request.BorrowerRequest;
import com.library.management.dto.response.BorrowerResponse;
import com.library.management.service.BorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/libraries/v1/borrowers")
public class BorrowerController {

  private final BorrowerService borrowerService;

  @PostMapping
  public ResponseEntity<BorrowerResponse> registerBorrower(
      @Valid @RequestBody BorrowerRequest borrowerRequest) throws URISyntaxException {

    log.info("Received request to register a new borrower: {}", borrowerRequest);

    BorrowerResponse borrowerResponse = borrowerService.registerBorrower(borrowerRequest);

    return ResponseEntity.created(new URI("/")).body(borrowerResponse);
  }

  @GetMapping
  public ResponseEntity<List<BorrowerResponse>> bookList() {
    log.info("Received request to list all borrowers");
    return ResponseEntity.ok(borrowerService.getAllBorrowers());
  }

}
