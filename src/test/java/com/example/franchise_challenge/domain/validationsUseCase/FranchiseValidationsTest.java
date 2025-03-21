package com.example.franchise_challenge.domain.validationsUseCase;

import com.example.franchise_challenge.domain.exceptions.FranchiseAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.FranchiseNotFoundException;
import com.example.franchise_challenge.domain.spi.IFranchisePersistencePort;
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
public class FranchiseValidationsTest {

    @Mock
    private IFranchisePersistencePort franchisePersistencePort;

    @InjectMocks
    private FranchiseValidations franchiseValidations;

    @Test
    public void test_validate_franchise_exists_when_franchise_exists_returns_empty_mono() {
        // Arrange
        Long franchiseId = 1L;

        Mockito.when(franchisePersistencePort.existsFranchiseById(franchiseId)).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(franchiseValidations.validateFranchiseExists(franchiseId))
                .verifyComplete();
    }

    @Test
    public void test_validate_franchise_exists_when_franchise_does_not_exist_throws_exception() {
        // Arrange
        Long franchiseId = 1L;

        Mockito.when(franchisePersistencePort.existsFranchiseById(franchiseId)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(franchiseValidations.validateFranchiseExists(franchiseId))
                .expectErrorMatches(throwable ->
                        throwable instanceof FranchiseNotFoundException &&
                                throwable.getMessage().equals(String.format(DomainConstants.FRANCHISE_NOT_FOUND, franchiseId)))
                .verify();
    }

    @Test
    public void test_validate_franchise_does_not_exist_when_franchise_name_does_not_exist() {
        // Arrange
        String franchiseName = "New Franchise";

        Mockito.when(franchisePersistencePort.existsFranchiseByName(franchiseName)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(franchiseValidations.validateFranchiseDoesNotExist(franchiseName))
                .verifyComplete();

        Mockito.verify(franchisePersistencePort).existsFranchiseByName(franchiseName);
    }

    @Test
    public void test_validate_franchise_does_not_exist_when_franchise_name_already_exists() {
        // Arrange
        String franchiseName = "Existing Franchise";

        Mockito.when(franchisePersistencePort.existsFranchiseByName(franchiseName)).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(franchiseValidations.validateFranchiseDoesNotExist(franchiseName))
                .expectErrorMatches(throwable ->
                        throwable instanceof FranchiseAlreadyExistsException &&
                                throwable.getMessage().equals(DomainConstants.FRANCHISE_ALREADY_EXISTS))
                .verify();

        Mockito.verify(franchisePersistencePort).existsFranchiseByName(franchiseName);
    }
}
