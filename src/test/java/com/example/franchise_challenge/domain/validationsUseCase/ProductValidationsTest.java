package com.example.franchise_challenge.domain.validationsUseCase;

import com.example.franchise_challenge.domain.exceptions.InvalidStockException;
import com.example.franchise_challenge.domain.exceptions.ProductAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.ProductNotFoundException;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.domain.utils.constants.DomainConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ProductValidationsTest {

    @Mock
    private IProductPersistencePort productPersistencePort;

    @InjectMocks
    private ProductValidations productValidations;

    @Test
    public void test_validate_product_does_not_exist_when_product_name_does_not_exist() {
        // Arrange
        String productName = "NonExistentProduct";

        Mockito.when(productPersistencePort.existsProductByName(productName))
                .thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(productValidations.validateProductDoesNotExist(productName))
                .verifyComplete();

        Mockito.verify(productPersistencePort).existsProductByName(productName);
    }

    @Test
    public void test_validate_product_does_not_exist_when_product_name_already_exists() {
        // Arrange
        String productName = "ExistingProduct";

        Mockito.when(productPersistencePort.existsProductByName(productName))
                .thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(productValidations.validateProductDoesNotExist(productName))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductAlreadyExistsException &&
                                throwable.getMessage().equals(DomainConstants.PRODUCT_ALREADY_EXISTS))
                .verify();

        Mockito.verify(productPersistencePort).existsProductByName(productName);
    }

    @Test
    public void test_validate_stock_returns_empty_when_stock_greater_than_min() {
        // Arrange
        int validStock = 1;

        // Act & Assert
        StepVerifier.create(productValidations.validateStock(validStock))
                .verifyComplete();
    }

    @Test
    public void test_validate_stock_returns_error_when_stock_equals_min() {
        // Arrange
        int invalidStock = DomainConstants.MIN_STOCK;

        // Act & Assert
        StepVerifier.create(productValidations.validateStock(invalidStock))
                .expectErrorMatches(throwable -> throwable instanceof InvalidStockException
                        && throwable.getMessage().equals(DomainConstants.INVALID_STOCK))
                .verify();
    }

    @Test
    public void test_returns_empty_mono_when_product_exists() {
        // Arrange
        Long productId = 1L;

        Mockito.when(productPersistencePort.existsProductById(productId)).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(productValidations.validateProductExistsById(productId))
                .verifyComplete();

        Mockito.verify(productPersistencePort).existsProductById(productId);
    }

    @Test
    public void test_throws_product_not_found_exception_when_product_does_not_exist() {
        // Arrange
        Long productId = 1L;

        Mockito.when(productPersistencePort.existsProductById(productId)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(productValidations.validateProductExistsById(productId))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundException &&
                                throwable.getMessage().equals(String.format(DomainConstants.PRODUCT_NOT_FOUND, productId)))
                .verify();

        Mockito.verify(productPersistencePort).existsProductById(productId);
    }
}
