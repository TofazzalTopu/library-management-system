package com.library.management.controller;

import com.library.management.annotation.APIDocumentation;
import com.library.management.constants.Constants;
import com.library.management.dto.request.BorrowerRequest;
import com.library.management.dto.response.BorrowerResponse;
import com.library.management.service.BorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.BORROWER_API_ENDPOINT)
public class BorrowerController {

  private final BorrowerService borrowerService;

  @PostMapping
  @APIDocumentation
  public ResponseEntity<BorrowerResponse> registerBorrower(
      @Valid @RequestBody BorrowerRequest borrowerRequest) {

    log.info("Received request to register a new borrower: {}", borrowerRequest);

    BorrowerResponse borrowerResponse = borrowerService.registerBorrower(borrowerRequest);

    return ResponseEntity.ok(borrowerResponse);
  }

  @GetMapping
  @APIDocumentation
  public ResponseEntity<Page<BorrowerResponse>> borrowerList(Pageable pageable) {

    log.info("Received request to list borrowers page={} size={}",
            pageable.getPageNumber(), pageable.getPageSize());

    return ResponseEntity.ok(borrowerService.getAllBorrowers(pageable));
  }

}
