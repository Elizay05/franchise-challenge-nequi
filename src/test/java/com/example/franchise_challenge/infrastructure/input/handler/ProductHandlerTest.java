package com.example.franchise_challenge.infrastructure.input.handler;

import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.ProductRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.StockUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.response.FranchiseStockResponse;
import com.example.franchise_challenge.application.handler.IProductRestHandler;
import com.example.franchise_challenge.infrastructure.entrypoints.handler.ProductHandler;
import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductHandlerTest {

    @Mock
    private IProductRestHandler productRestHandler;

    @Mock
    private Validator validator;

    @InjectMocks
    private ProductHandler productHandler;

    @Test
    public void test_valid_product_request_returns_created_status() {
        // Arrange
        ServerRequest request = MockServerRequest.builder()
                .body(Mono.just(new ProductRequest("Test Product", 1L, 10)));

        when(validator.validate(any(ProductRequest.class))).thenReturn(Collections.emptySet());
        when(productRestHandler.createProduct(any(ProductRequest.class))).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> response = productHandler.createProduct(request);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.CREATED)
                .verifyComplete();

        verify(productRestHandler).createProduct(any(ProductRequest.class));
    }

    @Test
    public void test_invalid_product_request_returns_bad_request() {
        // Arrange
        ProductRequest invalidRequest = new ProductRequest("", null, 10);
        ServerRequest request = MockServerRequest.builder()
                .body(Mono.just(invalidRequest));

        Set<ConstraintViolation<ProductRequest>> violations = new HashSet<>();
        ConstraintViolation<ProductRequest> nameViolation = Mockito.mock(ConstraintViolation.class);
        ConstraintViolation<ProductRequest> branchIdViolation = Mockito.mock(ConstraintViolation.class);

        jakarta.validation.Path namePath = Mockito.mock(jakarta.validation.Path.class);
        jakarta.validation.Path branchIdPath = Mockito.mock(jakarta.validation.Path.class);

        when(nameViolation.getPropertyPath()).thenReturn(namePath);
        when(nameViolation.getMessage()).thenReturn("is required");
        when(branchIdViolation.getPropertyPath()).thenReturn(branchIdPath);
        when(branchIdViolation.getMessage()).thenReturn("is required");

        violations.add(nameViolation);
        violations.add(branchIdViolation);

        when(validator.validate(any(ProductRequest.class))).thenReturn(violations);

        // Act
        Mono<ServerResponse> response = productHandler.createProduct(request);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> {
                    return serverResponse.statusCode() == HttpStatus.BAD_REQUEST &&
                            serverResponse.headers().getContentType().equals(MediaType.APPLICATION_JSON);
                })
                .verifyComplete();

        verify(productRestHandler, never()).createProduct(any(ProductRequest.class));
    }

    @Test
    public void test_delete_product_success() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        when(productRestHandler.deleteProduct(productId, branchId))
                .thenReturn(Mono.empty());

        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.PRODUCT_ID, productId.toString())
                .pathVariable(InputConstants.BRANCH_ID, branchId.toString())
                .build();

        // Act
        Mono<ServerResponse> responseMono = productHandler.deleteProduct(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();

        verify(productRestHandler).deleteProduct(productId, branchId);
    }

    @Test
    public void test_update_stock_success() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int stockValue = 10;

        ServerRequest requestMock = Mockito.mock(ServerRequest.class);
        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest(stockValue);

        when(requestMock.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(requestMock.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());
        when(requestMock.bodyToMono(StockUpdateRequest.class)).thenReturn(Mono.just(stockUpdateRequest));
        when(validator.validate(stockUpdateRequest)).thenReturn(Collections.emptySet());
        when(productRestHandler.updateStock(productId, branchId, stockValue)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = productHandler.updateStock(requestMock);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();

        verify(productRestHandler).updateStock(productId, branchId, stockValue);
    }

    @Test
    public void test_update_stock_invalid_value() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int invalidStockValue = -5;

        ServerRequest requestMock = Mockito.mock(ServerRequest.class);
        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest(invalidStockValue);

        Set<ConstraintViolation<StockUpdateRequest>> violations = new HashSet<>();
        ConstraintViolation<StockUpdateRequest> violation = Mockito.mock(ConstraintViolation.class);
        jakarta.validation.Path path = Mockito.mock(jakarta.validation.Path.class);

        when(requestMock.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(requestMock.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());
        when(requestMock.bodyToMono(StockUpdateRequest.class)).thenReturn(Mono.just(stockUpdateRequest));

        when(path.toString()).thenReturn("stock");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must be greater than or equal to 0");
        violations.add(violation);

        when(validator.validate(stockUpdateRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> result = productHandler.updateStock(requestMock);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST;
                })
                .verifyComplete();

        verify(productRestHandler, never()).updateStock(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    public void test_get_products_returns_franchise_stock_for_valid_id() {
        // Arrange
        Long franchiseId = 1L;

        FranchiseStockResponse mockResponse = new FranchiseStockResponse();
        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.FRANCHISE_ID, franchiseId.toString())
                .build();

        when(productRestHandler.getAllProductStockByFranchise(franchiseId))
                .thenReturn(Mono.just(mockResponse));

        // Act
        Mono<ServerResponse> result = productHandler.getProducts(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.OK;
                })
                .verifyComplete();

        verify(productRestHandler).getAllProductStockByFranchise(franchiseId);
    }

    @Test
    public void test_update_name_with_valid_request() {
        // Arrange
        Long productId = 1L;
        String newName = "New Product Name";
        NameUpdateRequest nameRequest = new NameUpdateRequest(newName);

        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.PRODUCT_ID, productId.toString())
                .body(Mono.just(nameRequest));

        Set<ConstraintViolation<NameUpdateRequest>> emptyViolations = Collections.emptySet();
        when(validator.validate(nameRequest)).thenReturn(emptyViolations);

        when(productRestHandler.updateName(productId, newName))
                .thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = productHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();

        verify(productRestHandler).updateName(productId, newName);
    }

    @Test
    public void test_update_name_with_blank_or_null_name() {
        // Arrange
        Long productId = 1L;
        NameUpdateRequest nameRequest = new NameUpdateRequest("");

        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.PRODUCT_ID, productId.toString())
                .body(Mono.just(nameRequest));

        Set<ConstraintViolation<NameUpdateRequest>> violations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(nameRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> result = productHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();

        verify(productRestHandler, never()).updateName(anyLong(), anyString());
    }
}
