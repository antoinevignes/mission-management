package com.antoinevignes.missionmanagement.exception;

import com.antoinevignes.missionmanagement.exception.aircraft.AircraftAlreadyExistsException;
import com.antoinevignes.missionmanagement.exception.aircraft.AircraftNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AircraftAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAircraftAlreadyExists(
      AircraftAlreadyExistsException ex, HttpServletRequest request) {

    ErrorResponse response =
        new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI(),
            null);

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    ErrorResponse response =
        new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Validation failed.",
            request.getRequestURI(),
            errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(AircraftNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAircraftNotFound(
      AircraftNotFoundException ex, HttpServletRequest request) {

    ErrorResponse response =
        new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI(),
            null);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }
}
