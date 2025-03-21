package com.example.franchise_challenge.infrastructure.exceptionHandler;

import com.example.franchise_challenge.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(FranchiseAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleFranchiseAlreadyExistsException(FranchiseAlreadyExistsException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.CONFLICT.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }
    @ExceptionHandler(BranchAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleBranchAlreadyExistsException(BranchAlreadyExistsException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.CONFLICT.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }
    @ExceptionHandler(FranchiseNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleFranchiseNotFoundException(FranchiseNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBranchNotFoundException(BranchNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.CONFLICT.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }
    @ExceptionHandler(InvalidStockException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidStockException(InvalidStockException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(ProductNotFoundInBranchException.class)
    public ResponseEntity<ExceptionResponse> handleProductNotFoundInBranchException(ProductNotFoundInBranchException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
}
