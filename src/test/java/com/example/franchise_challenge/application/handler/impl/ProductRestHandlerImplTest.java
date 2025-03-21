package com.example.franchise_challenge.application.handler.impl;


import com.example.franchise_challenge.application.dto.request.ProductRequest;
import com.example.franchise_challenge.application.dto.response.FranchiseStockResponse;
import com.example.franchise_challenge.application.mapper.IFranchiseStockResponseMapper;
import com.example.franchise_challenge.application.mapper.IProductRequestMapper;
import com.example.franchise_challenge.domain.api.IProductServicePort;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.model.ProductModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRestHandlerImplTest {

    @Mock
    private IProductServicePort productServicePort;

    @Mock
    private IProductRequestMapper productRequestMapper;

    @Mock
    private IFranchiseStockResponseMapper franchiseStockResponseMapper;

    @InjectMocks
    private ProductRestHandlerImpl productRestHandler;

    @Test
    public void test_valid_product_request_is_mapped_to_product_model() {
        // Arrange
        ProductRequest productRequest = new ProductRequest("Test Product", 1L, 10);
        ProductModel expectedProductModel = new ProductModel(null, "Test Product", 1L, 10);

        when(productRequestMapper.toModel(productRequest)).thenReturn(expectedProductModel);
        when(productServicePort.createProduct(expectedProductModel)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productRestHandler.createProduct(productRequest);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productRequestMapper).toModel(productRequest);
        verify(productServicePort).createProduct(expectedProductModel);
    }

    @Test
    public void test_delete_product_successfully() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        Mockito.when(productServicePort.deleteProduct(productId, branchId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productRestHandler.deleteProduct(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(productServicePort).deleteProduct(productId, branchId);
    }

    @Test
    public void test_update_stock_successfully() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int stock = 10;

        Mockito.when(productServicePort.updateStock(productId, branchId, stock))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productRestHandler.updateStock(productId, branchId, stock);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(productServicePort).updateStock(productId, branchId, stock);
    }

    @Test
    public void test_returns_mapped_franchise_stock_response() {
        // Arrange
        Long franchiseId = 1L;
        FranchiseStockModel franchiseStockModel = FranchiseStockModel.builder().build();
        franchiseStockModel.setFranchiseId(franchiseId);

        FranchiseStockResponse expectedResponse = new FranchiseStockResponse();
        expectedResponse.setFranchiseId(franchiseId);

        Mockito.when(productServicePort.getProductsByFranchise(franchiseId)).thenReturn(Mono.just(franchiseStockModel));
        Mockito.when(franchiseStockResponseMapper.toResponse(franchiseStockModel)).thenReturn(expectedResponse);

        // Act
        Mono<FranchiseStockResponse> result = productRestHandler.getAllProductStockByFranchise(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedResponse)
                .verifyComplete();

        Mockito.verify(productServicePort).getProductsByFranchise(franchiseId);
        Mockito.verify(franchiseStockResponseMapper).toResponse(franchiseStockModel);
    }

    @Test
    public void test_update_name_success() {
        // Arrange
        Long productId = 1L;
        String name = "New Product Name";

        Mockito.when(productServicePort.updateName(productId, name)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productRestHandler.updateName(productId, name);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(productServicePort).updateName(productId, name);
    }
}
