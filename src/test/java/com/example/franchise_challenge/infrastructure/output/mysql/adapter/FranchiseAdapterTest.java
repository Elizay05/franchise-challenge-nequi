package com.example.franchise_challenge.infrastructure.output.mysql.adapter;

import com.example.franchise_challenge.domain.model.FranchiseModel;
import com.example.franchise_challenge.infrastructure.adapters.mysql.adapter.FranchiseAdapter;
import com.example.franchise_challenge.infrastructure.adapters.mysql.entity.FranchiseEntity;
import com.example.franchise_challenge.infrastructure.adapters.mysql.mapper.IFranchiseEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.mysql.repository.IFranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class FranchiseAdapterTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @Mock
    private IFranchiseEntityMapper franchiseEntityMapper;

    @InjectMocks
    private FranchiseAdapter franchiseAdapter;

    @Test
    public void test_save_franchise_successfully() {
        // Arrange
        FranchiseModel franchiseModel = new FranchiseModel(1L, "Test Franchise");
        FranchiseEntity franchiseEntity = new FranchiseEntity(1L, "Test Franchise");

        Mockito.when(franchiseEntityMapper.toEntity(franchiseModel)).thenReturn(franchiseEntity);
        Mockito.when(franchiseRepository.save(franchiseEntity)).thenReturn(Mono.just(franchiseEntity));

        // Act
        Mono<Void> result = franchiseAdapter.saveFranchise(franchiseModel);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(franchiseEntityMapper).toEntity(franchiseModel);
        Mockito.verify(franchiseRepository).save(franchiseEntity);
    }

    @Test
    public void test_returns_true_when_franchise_exists_by_name() {
        // Arrange
        String franchiseName = "Test Franchise";

        Mockito.when(franchiseRepository.existsFranchiseEntitiesByName(franchiseName))
                .thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = franchiseAdapter.existsFranchiseByName(franchiseName);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(franchiseRepository).existsFranchiseEntitiesByName(franchiseName);
    }

    @Test
    public void test_returns_true_when_franchise_exists_by_id() {
        // Arrange
        Long franchiseId = 1L;

        Mockito.when(franchiseRepository.existsById(franchiseId)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = franchiseAdapter.existsFranchiseById(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(franchiseRepository).existsById(franchiseId);
    }

    @Test
    public void test_update_name_successfully() {
        // Arrange
        Long franchiseId = 1L;
        String name = "New Franchise Name";

        Mockito.when(franchiseRepository.updateName(franchiseId, name)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = franchiseAdapter.updateName(franchiseId, name);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(franchiseRepository).updateName(franchiseId, name);
    }
}
