package com.sinnts.grading.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.sinnts.grading.exceptions.ErrorCodes.ACCOUNT_LOCKED;
import static com.sinnts.grading.exceptions.ErrorCodes.BAD_CREDENTIALS;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class CustomExceptionHandler {

/*
  @ExceptionHandler(LockedException.class)
  public final ResponseEntity<ExceptionResponse> handleLockedException(LockedException exception) {
    return ResponseEntity
        .status(UNAUTHORIZED)
        .body(
            ExceptionResponse.builder()
                .errorCode(ACCOUNT_LOCKED.getCode())
                .errorDescription(ACCOUNT_LOCKED.getDescription())
                .error(exception.getMessage())
                .build()
        );
  }


  @ExceptionHandler(DisabledException.class)
  public final ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException exception) {
    return ResponseEntity
        .status(UNAUTHORIZED)
        .body(
            ExceptionResponse.builder()
                .errorCode(ACCOUNT_DISABLED.getCode())
                .errorDescription(ACCOUNT_DISABLED.getDescription())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(BadCredentialsException.class)
  public final ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException exception) {
    return ResponseEntity
        .status(UNAUTHORIZED)
        .body(
            ExceptionResponse.builder()
                .errorCode(BAD_CREDENTIALS.getCode())
                .errorDescription(BAD_CREDENTIALS.getDescription())
                .error(BAD_CREDENTIALS.getDescription())
                .build()
        );
  }

  @ExceptionHandler(MessagingException.class)
  public final ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException exception) {
    return ResponseEntity
        .status(INTERNAL_SERVER_ERROR)
        .body(
            ExceptionResponse.builder()
                .error(exception.getMessage())
                .build()
        );
  }
*/

  @ExceptionHandler(ResourceNotFoundException.class)
  public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
    return ResponseEntity
        .status(NOT_FOUND)
        .body(
            ExceptionResponse.builder()
                .errorCode(NOT_FOUND.value())
                .errorDescription(exception.getMessage())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(InvalidResourceException.class)
  public final ResponseEntity<ExceptionResponse> handleInvalidResourceException(InvalidResourceException exception) {
    return ResponseEntity
        .status(BAD_REQUEST)
        .body(
            ExceptionResponse.builder()
                .errorCode(BAD_REQUEST.value())
                .errorDescription(exception.getMessage())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    Set<String> errors = new HashSet<>();

    exception.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> errors.add(error.getDefaultMessage())
        );

    return ResponseEntity
        .status(BAD_REQUEST)
        .body(
            ExceptionResponse.builder()
                .validationErrors(errors)
                .build()
        );
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ExceptionResponse> handleException(Exception exception) {

    // log exception
//    log.error(exception.getMessage());
    return ResponseEntity
        .status(INTERNAL_SERVER_ERROR)
        .body(
            ExceptionResponse.builder()
                .errorDescription("Internal Server Error, please contact the administrator")
                .error(exception.getMessage())
                .build()
        );
  }
}
