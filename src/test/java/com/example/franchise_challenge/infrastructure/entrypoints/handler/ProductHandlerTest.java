package com.example.franchise_challenge.infrastructure.entrypoints.handler;

import com.example.franchise_challenge.domain.api.IProductServicePort;
import com.example.franchise_challenge.domain.exceptions.*;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.ProductRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.StockUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.response.FranchiseStockResponse;
import com.example.franchise_challenge.infrastructure.entrypoints.mapper.IFranchiseStockResponseMapper;
import com.example.franchise_challenge.infrastructure.entrypoints.mapper.IProductRequestMapper;
import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductHandlerTest {

    @Mock
    private IProductServicePort productServicePort;

    @Mock
    private IProductRequestMapper productRequestMapper;

    @Mock
    private IFranchiseStockResponseMapper franchiseStockResponseMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private ProductHandler productHandler;

    @Test
    public void test_valid_product_request_returns_created_status() {
        // Arrange
        ProductRequest productRequest = new ProductRequest("Test Product", 1L, 10);
        ProductModel productModel = new ProductModel(null, "Test Product", 1L, 10);

        ServerRequest request = mock(ServerRequest.class);

        when(request.bodyToMono(ProductRequest.class)).thenReturn(Mono.just(productRequest));
        when(validator.validate(productRequest)).thenReturn(Collections.emptySet());
        when(productRequestMapper.toModel(productRequest)).thenReturn(productModel);
        when(productServicePort.createProduct(productModel)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = productHandler.createProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CREATED)
                .verifyComplete();

        verify(validator).validate(productRequest);
        verify(productRequestMapper).toModel(productRequest);
        verify(productServicePort).createProduct(productModel);
    }

    @Test
    public void test_invalid_product_request_returns_bad_request() {
        // Arrange
        ProductRequest productRequest = new ProductRequest("", null, -1);
        ServerRequest request = mock(ServerRequest.class);

        Set<ConstraintViolation<ProductRequest>> violations = new HashSet<>();
        ConstraintViolation<ProductRequest> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("name");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn(InputConstants.IS_REQUIRED);
        violations.add(violation);

        when(request.bodyToMono(ProductRequest.class)).thenReturn(Mono.just(productRequest));
        when(validator.validate(productRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> result = productHandler.createProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST;
                })
                .verifyComplete();

        verify(validator).validate(productRequest);
        verify(productServicePort, never()).createProduct(any());
    }

    @Test
    public void test_invalid_branch_id_throws_branch_not_found_exception() {
        // Arrange
        ProductRequest productRequest = new ProductRequest("Test Product", 999L, 10);
        BranchNotFoundException exception = new BranchNotFoundException("Branch not found");

        ServerRequest request = mock(ServerRequest.class);

        when(request.bodyToMono(ProductRequest.class)).thenReturn(Mono.just(productRequest));
        when(validator.validate(productRequest)).thenReturn(Collections.emptySet());
        when(productRequestMapper.toModel(productRequest)).thenReturn(new ProductModel(null, "Test Product", 999L, 10));
        when(productServicePort.createProduct(any(ProductModel.class))).thenReturn(Mono.error(exception));

        // Act
        Mono<ServerResponse> result = productHandler.createProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    @Test
    public void test_product_already_exists_exception_returns_conflict_status() {
        // Arrange
        ProductRequest productRequest = new ProductRequest("Existing Product", 1L, 10);
        ProductModel productModel = new ProductModel(null, "Existing Product", 1L, 10);

        ServerRequest request = mock(ServerRequest.class);

        when(request.bodyToMono(ProductRequest.class)).thenReturn(Mono.just(productRequest));
        when(validator.validate(productRequest)).thenReturn(Collections.emptySet());
        when(productRequestMapper.toModel(productRequest)).thenReturn(productModel);
        when(productServicePort.createProduct(productModel)).thenReturn(Mono.error(
                new ProductAlreadyExistsException("Product already exists")));

        // Act
        Mono<ServerResponse> result = productHandler.createProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CONFLICT)
                .verifyComplete();

        verify(validator).validate(productRequest);
        verify(productRequestMapper).toModel(productRequest);
        verify(productServicePort).createProduct(productModel);
    }

    @Test
    public void test_invalid_stock_value_throws_invalid_stock_exception() {
        // Arrange
        ProductRequest productRequest = new ProductRequest("Existing Product", 1L, 10);
        ProductModel productModel = new ProductModel(null, "Existing Product", 1L, 10);

        ServerRequest request = mock(ServerRequest.class);

        when(request.bodyToMono(ProductRequest.class)).thenReturn(Mono.just(productRequest));
        when(validator.validate(productRequest)).thenReturn(Collections.emptySet());
        when(productRequestMapper.toModel(productRequest)).thenReturn(productModel);
        when(productServicePort.createProduct(productModel)).thenReturn(Mono.error(
                new InvalidStockException("Invalid stock value")));

        // Act
        Mono<ServerResponse> result = productHandler.createProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();

        verify(validator).validate(productRequest);
        verify(productRequestMapper).toModel(productRequest);
        verify(productServicePort).createProduct(productModel);
    }

    @Test
    public void test_unexpected_exception_returns_internal_server_error_create_product() {
        // Arrange
        ProductRequest validRequest = new ProductRequest("Test Product", 1L, 10);
        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(validRequest));

        Set<ConstraintViolation<ProductRequest>> emptyViolations = Collections.emptySet();

        when(validator.validate(validRequest)).thenReturn(emptyViolations);
        when(productRequestMapper.toModel(validRequest)).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        Mono<ServerResponse> responseMono = productHandler.createProduct(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();

        verify(validator).validate(validRequest);
        verify(productRequestMapper).toModel(validRequest);
    }

    @Test
    public void test_delete_product_returns_no_content() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());

        when(productServicePort.deleteProduct(productId, branchId)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = productHandler.deleteProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();

        verify(productServicePort).deleteProduct(productId, branchId);
    }

    @Test
    public void test_branch_not_found_exception_returns_not_found() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());

        when(productServicePort.deleteProduct(productId, branchId))
                .thenReturn(Mono.error(new BranchNotFoundException("Branch not found")));

        // Act
        Mono<ServerResponse> result = productHandler.deleteProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.NOT_FOUND;
                })
                .verifyComplete();

        verify(productServicePort).deleteProduct(productId, branchId);
    }

    @Test
    public void test_product_not_found_in_branch_exception_returns_not_found() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());

        when(productServicePort.deleteProduct(productId, branchId))
                .thenReturn(Mono.error(new ProductNotFoundInBranchException("Product not found in branch")));

        // Act
        Mono<ServerResponse> result = productHandler.deleteProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.NOT_FOUND;
                })
                .verifyComplete();

        verify(productServicePort).deleteProduct(productId, branchId);
    }

    @Test
    public void test_unexpected_exception_returns_internal_server_error_delete_product() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());

        when(productServicePort.deleteProduct(productId, branchId)).thenReturn(Mono.error(new RuntimeException()));

        // Act
        Mono<ServerResponse> result = productHandler.deleteProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();

        verify(productServicePort).deleteProduct(productId, branchId);
    }

    @Test
    public void test_update_stock_returns_ok() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int stockValue = 10;

        IProductServicePort productServicePort = mock(IProductServicePort.class);
        Validator validator = mock(Validator.class);
        ProductHandler productHandler = new ProductHandler(productServicePort, null, null, validator);

        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest(stockValue);
        ServerRequest request = mock(ServerRequest.class);

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());
        when(request.bodyToMono(StockUpdateRequest.class)).thenReturn(Mono.just(stockUpdateRequest));
        when(validator.validate(stockUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateStock(productId, branchId, stockValue)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = productHandler.updateStock(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(productServicePort).updateStock(productId, branchId, stockValue);
    }

    @Test
    public void test_update_stock_with_invalid_stock_returns_bad_request() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int invalidStock = -5;

        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest(invalidStock);
        ServerRequest request = mock(ServerRequest.class);

        Set<ConstraintViolation<StockUpdateRequest>> violations = new HashSet<>();
        ConstraintViolation<StockUpdateRequest> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("stock");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must be greater than or equal to minimum stock value");
        violations.add(violation);

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());
        when(request.bodyToMono(StockUpdateRequest.class)).thenReturn(Mono.just(stockUpdateRequest));
        when(validator.validate(stockUpdateRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> result = productHandler.updateStock(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST;
                })
                .verifyComplete();

        verify(productServicePort, never()).updateStock(any(), any(), anyInt());
    }

    @Test
    public void test_update_stock_with_branch_not_found_returns_not_found() {
        // Arrange
        Long productId = 1L;
        Long branchId = 999L;
        int stockValue = 10;

        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest(stockValue);
        ServerRequest request = mock(ServerRequest.class);

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());
        when(request.bodyToMono(StockUpdateRequest.class)).thenReturn(Mono.just(stockUpdateRequest));
        when(validator.validate(stockUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateStock(productId, branchId, stockValue))
                .thenReturn(Mono.error(new BranchNotFoundException("Branch not found")));

        // Act
        Mono<ServerResponse> result = productHandler.updateStock(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(productServicePort).updateStock(productId, branchId, stockValue);
    }

    @Test
    public void test_update_stock_with_product_not_found_in_branch_returns_not_found() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int stockValue = 10;

        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest(stockValue);
        ServerRequest request = mock(ServerRequest.class);

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());
        when(request.bodyToMono(StockUpdateRequest.class)).thenReturn(Mono.just(stockUpdateRequest));
        when(validator.validate(stockUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateStock(productId, branchId, stockValue))
                .thenReturn(Mono.error(new ProductNotFoundInBranchException("Product not found in branch")));

        // Act
        Mono<ServerResponse> result = productHandler.updateStock(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(productServicePort).updateStock(productId, branchId, stockValue);
    }

    @Test
    public void test_update_stock_with_invalid_stock_value_returns_bad_request() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int invalidStockValue = 0;

        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest(invalidStockValue);
        ServerRequest request = mock(ServerRequest.class);

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());
        when(request.bodyToMono(StockUpdateRequest.class)).thenReturn(Mono.just(stockUpdateRequest));
        when(validator.validate(stockUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateStock(productId, branchId, invalidStockValue))
                .thenReturn(Mono.error(new InvalidStockException("Invalid stock value")));

        // Act
        Mono<ServerResponse> result = productHandler.updateStock(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();

        verify(productServicePort).updateStock(productId, branchId, invalidStockValue);
    }

    @Test
    public void test_update_stock_with_unexpected_error_returns_internal_server_error() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int stockValue = 10;

        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest(stockValue);
        ServerRequest request = mock(ServerRequest.class);

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn(productId.toString());
        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn(branchId.toString());
        when(request.bodyToMono(StockUpdateRequest.class)).thenReturn(Mono.just(stockUpdateRequest));
        when(validator.validate(stockUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateStock(productId, branchId, stockValue)).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        Mono<ServerResponse> result = productHandler.updateStock(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();
    }

    @Test
    public void test_get_products_returns_ok() {
        // Arrange
        Long franchiseId = 1L;
        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.FRANCHISE_ID, franchiseId.toString())
                .build();

        FranchiseStockModel franchiseStockModel = new FranchiseStockModel(franchiseId, new ArrayList<>());
        FranchiseStockResponse franchiseStockResponse = new FranchiseStockResponse();

        when(productServicePort.getProductsByFranchise(franchiseId))
                .thenReturn(Mono.just(franchiseStockModel));
        when(franchiseStockResponseMapper.toResponse(franchiseStockModel))
                .thenReturn(franchiseStockResponse);

        // Act
        Mono<ServerResponse> result = productHandler.getProducts(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(productServicePort).getProductsByFranchise(franchiseId);
        verify(franchiseStockResponseMapper).toResponse(franchiseStockModel);
    }

    @Test
    public void test_get_products_with_franchise_not_found_returns_not_found() {
        // Arrange
        Long franchiseId = 999L;
        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.FRANCHISE_ID, franchiseId.toString())
                .build();

        when(productServicePort.getProductsByFranchise(franchiseId))
                .thenReturn(Mono.error(new FranchiseNotFoundException("Franchise not found")));

        // Act
        Mono<ServerResponse> result = productHandler.getProducts(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(productServicePort).getProductsByFranchise(franchiseId);
        verify(franchiseStockResponseMapper, never()).toResponse(any());
    }

    @Test
    public void test_get_products_with_unexpected_error_returns_internal_server_error() {
        // Arrange
        Long franchiseId = 1L;
        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.FRANCHISE_ID, franchiseId.toString())
                .build();

        when(productServicePort.getProductsByFranchise(franchiseId))
                .thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        // Act
        Mono<ServerResponse> result = productHandler.getProducts(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();

        verify(productServicePort).getProductsByFranchise(franchiseId);
    }

    @Test
    public void test_update_name_returns_ok() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("New Product Name");

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateName(1L, "New Product Name")).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = productHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(productServicePort).updateName(1L, "New Product Name");
    }

    @Test
    public void test_update_name_with_invalid_request_returns_bad_request() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("");

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Set.of(mock(ConstraintViolation.class)));

        // Act
        Mono<ServerResponse> result = productHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }

    @Test
    public void test_update_name_with_nonexistent_product_returns_not_found() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("Nonexistent Product");

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn("999");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateName(999L, "Nonexistent Product"))
                .thenReturn(Mono.error(new ProductNotFoundException("Product not found")));

        // Act
        Mono<ServerResponse> result = productHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(productServicePort).updateName(999L, "Nonexistent Product");
    }

    @Test
    public void test_update_name_when_product_already_exists() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("Existing Product Name");

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateName(1L, "Existing Product Name"))
                .thenReturn(Mono.error(new ProductAlreadyExistsException("Product name already exists")));

        // Act
        Mono<ServerResponse> result = productHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CONFLICT)
                .verifyComplete();

        verify(productServicePort).updateName(1L, "Existing Product Name");
    }

    @Test
    public void test_update_name_unexpected_exception_returns_internal_server_error() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("New Product Name");

        when(request.pathVariable(InputConstants.PRODUCT_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(productServicePort.updateName(1L, "New Product Name")).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        Mono<ServerResponse> result = productHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();
    }
}
