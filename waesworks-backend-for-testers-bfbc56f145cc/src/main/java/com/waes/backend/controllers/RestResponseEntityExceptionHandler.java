package com.waes.backend.controllers;

import com.waes.backend.exceptions.AlreadyRegisteredException;
import com.waes.backend.exceptions.ErrorDetails;
import com.waes.backend.exceptions.UsernameNotFoundException;
import com.waes.backend.logging.Logging;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Date;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler implements Logging {

  @ExceptionHandler(value = {UsernameNotFoundException.class})
  protected ResponseEntity<ErrorDetails> handleUsernameNotFoundException(UsernameNotFoundException ex,
      WebRequest request) {
    return createErrorDetailsResponseEntity(ex, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {AlreadyRegisteredException.class})
  protected ResponseEntity<ErrorDetails> handleAlreadyRegisteredException(AlreadyRegisteredException ex,
      WebRequest request) {
    return createErrorDetailsResponseEntity(ex, request, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = {IOException.class, Exception.class})
  protected ResponseEntity<ErrorDetails> handleAllExceptions(RuntimeException ex, WebRequest request) {
    return createErrorDetailsResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorDetails> createErrorDetailsResponseEntity(Exception ex, WebRequest request,
      HttpStatus httpStatus) {
    ErrorDetails errorDetails = new ErrorDetails(new Date(),
        httpStatus.value(),
        httpStatus.getReasonPhrase(),
        ex.getMessage(),
        request.getDescription(false));
    log().error("{}: {}", httpStatus, ex.getLocalizedMessage());
    return new ResponseEntity<>(errorDetails, httpStatus);
  }
}