package com.library.management.controller;

import com.library.management.annotation.APIDocumentation;
import com.library.management.constants.Constants;
import com.library.management.dto.request.BorrowRequest;
import com.library.management.dto.response.BorrowRecordResponse;
import com.library.management.dto.response.BorrowResponse;
import com.library.management.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.BORROW_API_ENDPOINT)
public class BorrowController {

  private final BorrowService borrowService;

  @PostMapping
  @APIDocumentation
  public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {

    log.info("Received borrow request. BorrowerId={}, BookId={}, Msg={}",
        request.getBorrowerId(), request.getBookId(), request.getBorrowMsg());

    BorrowResponse borrowResponse = borrowService.borrowBook(
        request.getBorrowerId(),
        request.getBookId(),
        request.getBorrowMsg()
    );

    log.info("Book borrowed successfully. BorrowRecordId={} \n BorrowResponse: {}", borrowResponse.getRecordId(), borrowResponse);

    return ResponseEntity.ok(borrowResponse);
  }

  @APIDocumentation
  @GetMapping("/{borrowRecordId}/return")
  public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long borrowRecordId) {

    log.info("Received return request for BorrowRecordId={}", borrowRecordId);

    BorrowResponse response = borrowService.returnBook(borrowRecordId);
    log.info("Book returned successfully. BorrowResponse: {}", response);

    return ResponseEntity.ok(response);
  }

  @APIDocumentation
  @GetMapping("/current")
  public ResponseEntity<List<BorrowRecordResponse>> getCurrentBorrowRecords() {

    log.info("Received request to fetch current borrow records");

    return ResponseEntity.ok(borrowService.getCurrentBorrowRecords());
  }

}
