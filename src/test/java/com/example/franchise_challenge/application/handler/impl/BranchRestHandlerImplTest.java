package com.example.franchise_challenge.application.handler.impl;

import com.example.franchise_challenge.application.dto.request.BranchRequest;
import com.example.franchise_challenge.application.mapper.IBranchRequestMapper;
import com.example.franchise_challenge.domain.api.IBranchServicePort;
import com.example.franchise_challenge.domain.model.BranchModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class BranchRestHandlerImplTest {

    @Mock
    private IBranchServicePort branchServicePort;

    @Mock
    private IBranchRequestMapper branchRequestMapper;

    @InjectMocks
    private BranchRestHandlerImpl branchRestHandler;

    @Test
    public void test_successfully_maps_branch_request_to_branch_model() {
        // Arrange
        BranchRequest branchRequest = new BranchRequest("Test Branch", 1);
        BranchModel expectedBranchModel = new BranchModel(null, "Test Branch", 1L);

        Mockito.when(branchRequestMapper.toModel(branchRequest)).thenReturn(expectedBranchModel);
        Mockito.when(branchServicePort.createBranch(expectedBranchModel)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = branchRestHandler.createBranch(branchRequest);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchRequestMapper).toModel(branchRequest);
        Mockito.verify(branchServicePort).createBranch(expectedBranchModel);
    }

    @Test
    public void test_update_name_with_valid_parameters() {
        // Arrange
        Long branchId = 1L;
        String name = "New Branch Name";

        Mockito.when(branchServicePort.updateName(branchId, name)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = branchRestHandler.updateName(branchId, name);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchServicePort).updateName(branchId, name);
    }
}
