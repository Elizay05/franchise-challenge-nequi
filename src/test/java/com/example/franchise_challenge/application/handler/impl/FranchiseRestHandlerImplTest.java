package com.example.franchise_challenge.application.handler.impl;

import com.example.franchise_challenge.application.dto.request.FranchiseRequest;
import com.example.franchise_challenge.application.mapper.IFranchiseRequestMapper;
import com.example.franchise_challenge.domain.api.IFranchiseServicePort;
import com.example.franchise_challenge.domain.model.FranchiseModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FranchiseRestHandlerImplTest {

    @Mock
    private IFranchiseServicePort franchiseServicePort;

    @Mock
    private IFranchiseRequestMapper franchiseRequestMapper;

    @InjectMocks
    private FranchiseRestHandlerImpl franchiseRestHandler;

    @Test
    public void test_create_franchise_successfully_maps_and_calls_service() {
        // Arrange
        FranchiseRequest franchiseRequest = new FranchiseRequest("Test Franchise");
        FranchiseModel franchiseModel = new FranchiseModel(null, "Test Franchise");


        when(franchiseRequestMapper.toModel(franchiseRequest)).thenReturn(franchiseModel);
        when(franchiseServicePort.createFranchise(franchiseModel)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = franchiseRestHandler.createFranchise(franchiseRequest);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseRequestMapper).toModel(franchiseRequest);
        verify(franchiseServicePort).createFranchise(franchiseModel);
    }

    @Test
    public void test_delegates_to_franchise_service_port_with_correct_parameters() {
        // Arrange
        Long franchiseId = 1L;
        String name = "New Franchise Name";

        when(franchiseServicePort.updateName(franchiseId, name)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = franchiseRestHandler.updateName(franchiseId, name);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseServicePort, times(1)).updateName(franchiseId, name);
    }
}
