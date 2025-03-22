package com.example.franchise_challenge.infrastructure.exceptionHandler;

import com.example.franchise_challenge.domain.exceptions.*;
import com.example.franchise_challenge.infrastructure.entrypoints.util.models.ExceptionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ControllerAdvisorTest {

    @InjectMocks
    private ControllerAdvisor controllerAdvisor;

    @Test
    public void test_handles_franchise_already_exists_exception_with_conflict_status() {
        // Arrange
        String errorMessage = "Franchise already exists";
        FranchiseAlreadyExistsException exception = new FranchiseAlreadyExistsException(errorMessage);

        // Act
        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleFranchiseAlreadyExistsException(exception);
        ExceptionResponse exceptionResponse = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(exceptionResponse);
        assertEquals(errorMessage, exceptionResponse.getMessage());
        assertEquals(HttpStatus.CONFLICT.toString(), exceptionResponse.getStatus());
        assertNotNull(exceptionResponse.getTimestamp());
    }

    @Test
    public void test_handle_branch_already_exists_exception_returns_conflict_status() {
        // Arrange
        String errorMessage = "Branch already exists";
        BranchAlreadyExistsException exception = new BranchAlreadyExistsException(errorMessage);

        // Act
        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleBranchAlreadyExistsException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.toString(), responseEntity.getBody().getStatus());
        assertNotNull(responseEntity.getBody().getTimestamp());
    }

    @Test
    public void test_returns_franchise_not_found_status_with_exception_message() {
        String errorMessage = "Franchise not found with id: 123";
        FranchiseNotFoundException exception = new FranchiseNotFoundException(errorMessage);

        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleFranchiseNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ExceptionResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.toString(), response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void test_returns_not_found_status_when_branch_not_found_exception_thrown() {
        // Arrange
        BranchNotFoundException exception = new BranchNotFoundException("Branch not found");

        // Act
        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleBranchNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Branch not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.toString(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void test_handler_returns_conflict_status() {
        // Arrange
        String errorMessage = "Product already exists";
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(errorMessage);

        // Act
        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleProductAlreadyExistsException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.toString(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void test_returns_bad_request_status_when_invalid_stock_exception_thrown() {
        // Arrange
        InvalidStockException exception = new InvalidStockException("Not enough stock");

        // Act
        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleInvalidStockException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Not enough stock", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void test_returns_product_not_found_in_branch_status_with_exception_message() {
        // Arrange
        ControllerAdvisor controllerAdvisor = new ControllerAdvisor();
        String errorMessage = "Product not found in branch";
        ProductNotFoundInBranchException exception = new ProductNotFoundInBranchException(errorMessage);

        // Act
        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleProductNotFoundInBranchException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.toString(), responseEntity.getBody().getStatus());
        assertNotNull(responseEntity.getBody().getTimestamp());
    }

    @Test
    public void test_handler_returns_product_not_found_status() {
        // Arrange
        String errorMessage = "Product not found";
        ProductNotFoundException exception = new ProductNotFoundException(errorMessage);

        // Act
        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleProductNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.toString(), responseEntity.getBody().getStatus());
        assertNotNull(responseEntity.getBody().getTimestamp());
    }
}
