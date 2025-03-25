package com.example.franchise_challenge.domain.validationsUseCase;

import com.example.franchise_challenge.domain.exceptions.ProductNotFoundInBranchException;
import com.example.franchise_challenge.domain.spi.IProductBranchPersistencePort;
import com.example.franchise_challenge.domain.utils.constants.DomainConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductBranchValidationsTest {

    @Mock
    private IProductBranchPersistencePort productBranchPersistencePort;

    @InjectMocks
    private ProductBranchValidations productBranchValidations;

    @Test
    public void test_when_product_exists_in_branch_returns_empty_mono() {
        // Arrange
        Long productId = 1L;
        Long branchId = 1L;

        when(productBranchPersistencePort.productExistsInBranch(productId, branchId))
                .thenReturn(Mono.just(true));

        // Act
        Mono<Void> result = productBranchValidations.validateProductExistsInBranch(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productBranchPersistencePort).productExistsInBranch(productId, branchId);
    }

    @Test
    public void test_when_product_does_not_exist_in_branch_returns_exception() {
        // Arrange
        Long productId = 1L;
        Long branchId = 1L;

        when(productBranchPersistencePort.productExistsInBranch(productId, branchId))
                .thenReturn(Mono.just(false));

        // Act
        Mono<Void> result = productBranchValidations.validateProductExistsInBranch(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductNotFoundInBranchException &&
                                throwable.getMessage().equals(DomainConstants.PRODUCT_NOT_FOUND_IN_BRANCH))
                .verify();

        verify(productBranchPersistencePort).productExistsInBranch(productId, branchId);
    }
}
