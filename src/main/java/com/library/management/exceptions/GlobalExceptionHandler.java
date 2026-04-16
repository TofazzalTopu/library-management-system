package com.library.management.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(BorrowerFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handle(BorrowerFailedException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(exception.getMessage()));
  }

  @ExceptionHandler(BookRegistrationFailureException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<Map<String, Object>> handle(BookRegistrationFailureException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(exception.getMessage()));
  }

  @ExceptionHandler(BorrowBookFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handle(BorrowBookFailedException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(exception.getMessage()));
  }

  @ExceptionHandler(ReturnBookFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handle(ReturnBookFailedException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(exception.getMessage()));
  }

  private Map<String, Object> errorResponse(String message) {
    log.error(message);
    Map<String, Object> response = new HashMap<>();
    response.put("message", message);
    response.put("status", 400);
    response.put("timestamp", LocalDateTime.now());
    return response;
  }

  private Error error(String message, String detailedErrorMessage) {
    return Error.builder()
        .message(message)
        .detailedErrorMessage(detailedErrorMessage)
        .build();
  }
}
