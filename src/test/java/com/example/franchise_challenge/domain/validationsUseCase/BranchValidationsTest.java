package com.example.franchise_challenge.domain.validationsUseCase;

import com.example.franchise_challenge.domain.exceptions.BranchAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.BranchNotFoundException;
import com.example.franchise_challenge.domain.spi.IBranchPersistencePort;
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
public class BranchValidationsTest {

    @Mock
    private IBranchPersistencePort branchPersistencePort;

    @InjectMocks
    private BranchValidations branchValidations;

    @Test
    public void test_validate_branch_exists_when_branch_exists_returns_empty_mono() {
        // Arrange
        Long branchId = 1L;

        Mockito.when(branchPersistencePort.existsBranchById(branchId)).thenReturn(Mono.just(true));

        // Act
        Mono<Void> result = branchValidations.validateBranchExists(branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchPersistencePort).existsBranchById(branchId);
    }

    @Test
    public void test_validate_branch_exists_when_branch_does_not_exist_throws_exception() {
        // Arrange
        Long branchId = 1L;

        Mockito.when(branchPersistencePort.existsBranchById(branchId)).thenReturn(Mono.just(false));

        // Act
        Mono<Void> result = branchValidations.validateBranchExists(branchId);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BranchNotFoundException
                        && throwable.getMessage().equals(String.format(DomainConstants.BRANCH_NOT_FOUND, branchId)))
                .verify();

        Mockito.verify(branchPersistencePort).existsBranchById(branchId);
    }

    @Test
    public void test_validate_branch_does_not_exist_when_branch_name_does_not_exist() {
        // Arrange
        String branchName = "New Branch";

        Mockito.when(branchPersistencePort.existsBranchByName(branchName)).thenReturn(Mono.just(false));

        // Act
        Mono<Void> result = branchValidations.validateBranchDoesNotExist(branchName);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchPersistencePort).existsBranchByName(branchName);
    }

    @Test
    public void test_validate_branch_does_not_exist_when_branch_name_already_exists() {
        // Arrange
        String branchName = "Existing Branch";

        Mockito.when(branchPersistencePort.existsBranchByName(branchName)).thenReturn(Mono.just(true));

        // Act
        Mono<Void> result = branchValidations.validateBranchDoesNotExist(branchName);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BranchAlreadyExistsException &&
                                throwable.getMessage().equals(DomainConstants.BRANCH_ALREADY_EXISTS))
                .verify();

        Mockito.verify(branchPersistencePort).existsBranchByName(branchName);
    }
}
