package com.example.franchise_challenge.infrastructure.adapters.jpa.adapter;

import com.example.franchise_challenge.domain.model.ProductBranchModel;
import com.example.franchise_challenge.infrastructure.adapters.jpa.entity.ProductBranchEntity;
import com.example.franchise_challenge.infrastructure.adapters.jpa.mapper.IProductBranchEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.jpa.repository.IProductBranchRepository;
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
public class ProductBranchAdapterTest {

    @Mock
    private IProductBranchRepository productBranchRepository;

    @Mock
    private IProductBranchEntityMapper productBranchEntityMapper;

    @InjectMocks
    private ProductBranchAdapter productBranchAdapter;

    @Test
    public void test_save_product_branch_successfully() {
        // Arrange
        ProductBranchModel productBranchModel = new ProductBranchModel(1L, 2L, 3L, 10);
        ProductBranchEntity productBranchEntity = new ProductBranchEntity(1L, 2L, 3L, 10);

        when(productBranchEntityMapper.toEntity(productBranchModel)).thenReturn(productBranchEntity);
        when(productBranchRepository.save(productBranchEntity)).thenReturn(Mono.just(productBranchEntity));

        // Act
        Mono<Void> result = productBranchAdapter.saveProductBranch(productBranchModel);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productBranchEntityMapper).toEntity(productBranchModel);
        verify(productBranchRepository).save(productBranchEntity);
    }

    @Test
    public void test_returns_true_when_product_exists_in_branch() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        when(productBranchRepository.existsByProductIdAndBranchId(productId, branchId))
                .thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = productBranchAdapter.productExistsInBranch(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(productBranchRepository).existsByProductIdAndBranchId(productId, branchId);
    }

    @Test
    public void test_delete_product_from_branch_success() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;


        when(productBranchRepository.deleteByProductIdAndBranchId(productId, branchId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productBranchAdapter.deleteProductFromBranch(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productBranchRepository).deleteByProductIdAndBranchId(productId, branchId);
    }

    @Test
    public void test_count_product_occurrences_returns_correct_count() {
        // Arrange
        Long productId = 1L;
        Long expectedCount = 3L;

        when(productBranchRepository.countByProductId(productId)).thenReturn(Mono.just(expectedCount));

        // Act
        Mono<Long> result = productBranchAdapter.countProductOccurrences(productId);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedCount)
                .verifyComplete();

        verify(productBranchRepository).countByProductId(productId);
    }

    @Test
    public void test_update_stock_successfully() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;
        int stock = 10;

        when(productBranchRepository.updateStock(productId, branchId, stock))
                .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productBranchAdapter.updateStock(productId, branchId, stock);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productBranchRepository).updateStock(productId, branchId, stock);
    }
}
