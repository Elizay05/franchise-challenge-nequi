package com.example.franchise_challenge.infrastructure.output.mysql.adapter;

import com.example.franchise_challenge.domain.model.BranchModel;
import com.example.franchise_challenge.infrastructure.adapters.mysql.adapter.BranchAdapter;
import com.example.franchise_challenge.infrastructure.adapters.mysql.entity.BranchEntity;
import com.example.franchise_challenge.infrastructure.adapters.mysql.mapper.IBranchEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.mysql.repository.IBranchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class BranchAdapterTest {

    @Mock
    private IBranchRepository branchRepository;

    @Mock
    private IBranchEntityMapper branchEntityMapper;

    @InjectMocks
    private BranchAdapter branchAdapter;

    @Test
    public void test_save_branch_maps_model_to_entity() {
        // Arrange
        BranchModel branchModel = new BranchModel(1L, "Test Branch", 2L);
        BranchEntity expectedEntity = new BranchEntity(1L, "Test Branch", 2L);

        Mockito.when(branchEntityMapper.toEntity(branchModel)).thenReturn(expectedEntity);
        Mockito.when(branchRepository.save(expectedEntity)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = branchAdapter.saveBranch(branchModel);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchEntityMapper).toEntity(branchModel);
        Mockito.verify(branchRepository).save(expectedEntity);
    }

    @Test
    public void test_returns_true_when_branch_exists_by_name() {
        // Arrange
        String branchName = "Test Branch";

        Mockito.when(branchRepository.existsBranchEntitiesByName(branchName)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = branchAdapter.existsBranchByName(branchName);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(branchRepository).existsBranchEntitiesByName(branchName);
    }

    @Test
    public void test_exists_branch_by_id_returns_true_when_branch_exists() {
        // Arrange
        Long branchId = 1L;

        Mockito.when(branchRepository.existsById(branchId)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = branchAdapter.existsBranchById(branchId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(branchRepository).existsById(branchId);
    }

    @Test
    public void test_update_name_successfully() {
        // Arrange
        Long branchId = 1L;
        String name = "New Branch Name";

        Mockito.when(branchRepository.updateName(branchId, name)).thenReturn(Mono.empty());

        BranchAdapter branchAdapter = new BranchAdapter(branchRepository, null);

        // Act
        Mono<Void> result = branchAdapter.updateName(branchId, name);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchRepository).updateName(branchId, name);
    }
}
