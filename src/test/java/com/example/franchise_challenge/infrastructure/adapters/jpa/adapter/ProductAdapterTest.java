package com.example.franchise_challenge.infrastructure.adapters.jpa.adapter;

import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.infrastructure.adapters.jpa.dto.ProductStockDTO;
import com.example.franchise_challenge.infrastructure.adapters.jpa.entity.ProductEntity;
import com.example.franchise_challenge.infrastructure.adapters.jpa.mapper.IProductEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.jpa.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductAdapterTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IProductEntityMapper productEntityMapper;

    @InjectMocks
    private ProductAdapter productAdapter;

    @Test
    public void test_save_product_successfully() {
        // Arrange
        ProductModel inputModel = new ProductModel(1L, "Test Product", 2L, 10);
        ProductEntity mappedEntity = new ProductEntity(1L, "Test Product");
        ProductModel outputModel = new ProductModel(1L, "Test Product", 2L, 10);

        when(productEntityMapper.toEntity(inputModel)).thenReturn(mappedEntity);
        when(productRepository.save(mappedEntity)).thenReturn(Mono.just(mappedEntity));
        when(productEntityMapper.toModel(mappedEntity)).thenReturn(outputModel);

        // Act
        Mono<ProductModel> result = productAdapter.saveProduct(inputModel);

        // Assert
        StepVerifier.create(result)
                .expectNext(outputModel)
                .verifyComplete();

        Mockito.verify(productEntityMapper).toEntity(inputModel);
        Mockito.verify(productRepository).save(mappedEntity);
        Mockito.verify(productEntityMapper).toModel(mappedEntity);
    }

    @Test
    public void test_returns_true_when_product_exists() {
        // Arrange
        String productName = "Existing Product";

        Mockito.when(productRepository.existsProductEntitiesByName(productName))
                .thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = productAdapter.existsProductByName(productName);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(productRepository).existsProductEntitiesByName(productName);
    }

    @Test
    public void test_delete_product_with_valid_id() {
        // Arrange
        Long validProductId = 1L;
        Mockito.when(productRepository.deleteById(validProductId)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productAdapter.deleteProduct(validProductId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(productRepository).deleteById(validProductId);
    }

    @Test
    public void test_returns_franchise_stock_model_with_correct_data() {
        // Arrange
        Long franchiseId = 1L;

        ProductStockDTO dto1 = new ProductStockDTO("Product1", "Branch1", 10);
        ProductStockDTO dto2 = new ProductStockDTO("Product2", "Branch2", 20);

        Mockito.when(productRepository.findTopStockedProductsByFranchiseId(franchiseId))
                .thenReturn(Flux.just(dto1, dto2));

        // Act
        Mono<FranchiseStockModel> result = productAdapter.getProductStockByFranchiseId(franchiseId);

        // Assert
        StepVerifier.create(result)
                .assertNext(model -> {
                    assertEquals(franchiseId, model.getFranchiseId());
                    assertEquals(2, model.getProducts().size());
                    assertEquals("Product1", model.getProducts().get(0).getName());
                    assertEquals("Branch1", model.getProducts().get(0).getBranchName());
                    assertEquals(10, model.getProducts().get(0).getStock());
                    assertEquals("Product2", model.getProducts().get(1).getName());
                    assertEquals("Branch2", model.getProducts().get(1).getBranchName());
                    assertEquals(20, model.getProducts().get(1).getStock());
                })
                .verifyComplete();
    }

    @Test
    public void test_update_name_with_valid_parameters() {
        // Arrange
        Long productId = 1L;
        String newName = "Updated Product Name";

        Mockito.when(productRepository.updateName(productId, newName)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productAdapter.updateName(productId, newName);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(productRepository).updateName(productId, newName);
    }

    @Test
    public void test_exists_product_by_id_returns_true_when_product_exists() {
        // Arrange
        Long productId = 1L;

        Mockito.when(productRepository.existsById(productId)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = productAdapter.existsProductById(productId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(productRepository).existsById(productId);
    }
}
