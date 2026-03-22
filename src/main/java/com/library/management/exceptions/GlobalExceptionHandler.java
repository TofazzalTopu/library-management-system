package com.library.management.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.CONFLICT)
  public Error handle(BookRegistrationFailureException exception) {
    log.error(exception.getMessage());
    return error(exception.getMessage(), exception.getCause().getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Error handle(BorrowBookFailedException exception) {
    log.error(exception.getMessage());
    return error(exception.getMessage(), exception.getCause().getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Error handle(ReturnBookFailedException exception) {
    log.error(exception.getMessage());
    return error(exception.getMessage(), exception.getCause().getMessage());
  }

  private Error error(String message) {
    return Error.builder()
        .message(message)
        .build();
  }

  private Error error(String message, String detailedErrorMessage) {
    return Error.builder()
        .message(message)
        .detailedErrorMessage(detailedErrorMessage)
        .build();
  }
}
