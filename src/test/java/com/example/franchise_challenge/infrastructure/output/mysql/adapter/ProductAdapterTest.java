package com.example.franchise_challenge.infrastructure.output.mysql.adapter;

import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.infrastructure.output.mysql.dto.ProductStockDTO;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.ProductBranchEntity;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.ProductEntity;
import com.example.franchise_challenge.infrastructure.output.mysql.mapper.IProductEntityMapper;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IProductBranchRepository;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductAdapterTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IProductBranchRepository productBranchRepository;

    @Mock
    private IProductEntityMapper productEntityMapper;

    @InjectMocks
    private ProductAdapter productAdapter;

    @Test
    public void test_save_product_successfully() {
        // Arrange
        ProductModel productModel = new ProductModel(null, "Test Product", 1L, 10);
        ProductEntity productEntity = new ProductEntity(null, "Test Product");
        ProductEntity savedProductEntity = new ProductEntity(1L, "Test Product");
        ProductBranchEntity productBranchEntity = ProductBranchEntity.builder()
                .productId(1L)
                .branchId(1L)
                .stock(10)
                .build();
        ProductBranchEntity savedProductBranchEntity = ProductBranchEntity.builder()
                .id(1L)
                .productId(1L)
                .branchId(1L)
                .stock(10)
                .build();

        when(productEntityMapper.toEntity(productModel)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(Mono.just(savedProductEntity));
        when(productBranchRepository.save(Mockito.any(ProductBranchEntity.class))).thenReturn(Mono.just(savedProductBranchEntity));

        // Act
        Mono<Void> result = productAdapter.saveProduct(productModel);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productEntityMapper).toEntity(productModel);
        verify(productRepository).save(productEntity);
        verify(productBranchRepository).save(Mockito.any(ProductBranchEntity.class));
    }

    @Test
    public void test_returns_true_when_product_by_name_exists() {
        // Arrange
        String productName = "Existing Product";

        when(productRepository.existsProductEntitiesByName(productName))
                .thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = productAdapter.existsProductByName(productName);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(productRepository).existsProductEntitiesByName(productName);
    }

    @Test
    public void test_returns_true_when_product_exists_in_branch() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        when(productBranchRepository.existsByProductIdAndBranchId(productId, branchId))
                .thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = productAdapter.productExistsInBranch(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(productBranchRepository).existsByProductIdAndBranchId(productId, branchId);
    }

    @Test
    public void test_delete_product_branch_association_success() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        when(productBranchRepository.deleteByProductIdAndBranchId(productId, branchId))
                .thenReturn(Mono.empty());
        when(productBranchRepository.countByProductId(productId))
                .thenReturn(Mono.just(1L));

        // Act
        Mono<Void> result = productAdapter.deleteProductFromBranch(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productBranchRepository).deleteByProductIdAndBranchId(productId, branchId);
        verify(productBranchRepository).countByProductId(productId);
        verify(productRepository, Mockito.never()).deleteById(productId);
    }

    @Test
    public void test_delete_product_when_last_branch_association() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        when(productBranchRepository.deleteByProductIdAndBranchId(productId, branchId)).thenReturn(Mono.empty());
        when(productBranchRepository.countByProductId(productId)).thenReturn(Mono.just(0L));
        when(productRepository.deleteById(productId)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productAdapter.deleteProductFromBranch(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productBranchRepository).deleteByProductIdAndBranchId(productId, branchId);
        verify(productBranchRepository).countByProductId(productId);
        verify(productRepository).deleteById(productId);
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
        Mono<Void> result = productAdapter.updateStock(productId, branchId, stock);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productBranchRepository).updateStock(productId, branchId, stock);
    }

    @Test
    public void test_returns_franchise_stock_model_with_correct_id_and_products() {
        // Arrange
        Long franchiseId = 1L;

        ProductStockDTO product1 = new ProductStockDTO("Product1", "Branch1", 10);
        ProductStockDTO product2 = new ProductStockDTO("Product2", "Branch2", 20);
        List<ProductStockDTO> productDTOs = List.of(product1, product2);

        when(productRepository.findTopStockedProductsByFranchiseId(franchiseId))
                .thenReturn(Flux.fromIterable(productDTOs));

        // Act
        Mono<FranchiseStockModel> result = productAdapter.getProductStockByFranchiseId(franchiseId);

        // Assert
        StepVerifier.create(result)
                .assertNext(franchiseStock -> {
                    assertEquals(franchiseId, franchiseStock.getFranchiseId());
                    assertEquals(2, franchiseStock.getProducts().size());
                    assertEquals("Product1", franchiseStock.getProducts().get(0).getName());
                    assertEquals("Branch1", franchiseStock.getProducts().get(0).getBranchName());
                    assertEquals(10, franchiseStock.getProducts().get(0).getStock());
                    assertEquals("Product2", franchiseStock.getProducts().get(1).getName());
                    assertEquals("Branch2", franchiseStock.getProducts().get(1).getBranchName());
                    assertEquals(20, franchiseStock.getProducts().get(1).getStock());
                })
                .verifyComplete();
    }

    @Test
    public void test_update_name_with_valid_inputs() {
        // Arrange
        Long productId = 1L;
        String newName = "Updated Product Name";

        when(productRepository.updateName(productId, newName)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productAdapter.updateName(productId, newName);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).updateName(productId, newName);
    }

    @Test
    public void test_returns_true_when_product_exists_by_id() {
        // Arrange
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = productAdapter.existsProductById(productId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(productRepository).existsById(productId);
    }
}
