package com.example.franchise_challenge.domain.usecase;

import com.example.franchise_challenge.domain.model.BranchModel;
import com.example.franchise_challenge.domain.spi.IBranchPersistencePort;
import com.example.franchise_challenge.domain.validationsUseCase.BranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.FranchiseValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BranchUseCaseTest {

    @Mock
    private IBranchPersistencePort branchPersistencePort;

    @Mock
    private FranchiseValidations franchiseValidations;

    @Mock
    private BranchValidations branchValidations;

    @InjectMocks
    private BranchUseCase branchUseCase;

    @Test
    public void test_create_branch_success() {
        // Arrange
        BranchModel branchModel = new BranchModel(null, "Test Branch", 1L);

        Mockito.when(branchValidations.validateBranchDoesNotExist("Test Branch"))
                .thenReturn(Mono.empty());
        Mockito.when(franchiseValidations.validateFranchiseExists(branchModel.getFranchiseId()))
                .thenReturn(Mono.empty());
        Mockito.when(branchPersistencePort.saveBranch(branchModel)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = branchUseCase.createBranch(branchModel);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchValidations, times(1)).validateBranchDoesNotExist(branchModel.getName());
        Mockito.verify(franchiseValidations, times(1)).validateFranchiseExists(branchModel.getFranchiseId());
        Mockito.verify(branchPersistencePort).saveBranch(branchModel);
    }

    @Test
    public void test_update_name_success_when_branch_exists_and_name_unique() {
        // Arrange
        Long branchId = 1L;
        String newName = "New Branch Name";

        Mockito.when(branchValidations.validateBranchExists(branchId))
                .thenReturn(Mono.empty());
        Mockito.when(branchValidations.validateBranchDoesNotExist(newName))
                .thenReturn(Mono.empty());
        Mockito.when(branchPersistencePort.updateName(branchId, newName)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = branchUseCase.updateName(branchId, newName);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchValidations).validateBranchExists(branchId);
        Mockito.verify(branchValidations).validateBranchDoesNotExist(newName);
        Mockito.verify(branchPersistencePort).updateName(branchId, newName);
    }
}
