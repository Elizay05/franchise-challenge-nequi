package com.example.franchise_challenge.domain.usecase;

import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.model.ProductBranchModel;
import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.spi.IProductBranchPersistencePort;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.domain.validationsUseCase.BranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.FranchiseValidations;
import com.example.franchise_challenge.domain.validationsUseCase.ProductBranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.ProductValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductUseCaseTest {

    @Mock
    private IProductPersistencePort productPersistencePort;

    @Mock
    private IProductBranchPersistencePort productBranchPersistencePort;

    @Mock
    private BranchValidations branchValidations;

    @Mock
    private ProductValidations productValidations;

    @Mock
    private FranchiseValidations franchiseValidations;

    @Mock
    private ProductBranchValidations productBranchValidations;

    @InjectMocks
    private ProductUseCase productUseCase;

    @Test
    public void test_create_product_success() {
        // Arrange
        ProductModel productModel = new ProductModel(null, "Test Product", 1L, 10);
        ProductModel savedProduct = new ProductModel(1L, "Test Product", 1L, 10);

        Mockito.when(branchValidations.validateBranchExists(1L)).thenReturn(Mono.empty());
        Mockito.when(productValidations.validateProductDoesNotExist("Test Product")).thenReturn(Mono.empty());
        Mockito.when(productValidations.validateStock(10)).thenReturn(Mono.empty());

        Mockito.when(productPersistencePort.saveProduct(productModel)).thenReturn(Mono.just(savedProduct));
        Mockito.when(productBranchPersistencePort.saveProductBranch(Mockito.any(ProductBranchModel.class)))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(productUseCase.createProduct(productModel))
                .verifyComplete();

        // Verify interactions
        Mockito.verify(branchValidations).validateBranchExists(1L);
        Mockito.verify(productValidations).validateProductDoesNotExist("Test Product");
        Mockito.verify(productValidations).validateStock(10);
        Mockito.verify(productPersistencePort).saveProduct(productModel);
        Mockito.verify(productBranchPersistencePort).saveProductBranch(Mockito.any(ProductBranchModel.class));
    }

    @Test
    public void test_delete_product_from_branch_successfully() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        Mockito.when(branchValidations.validateBranchExists(branchId)).thenReturn(Mono.empty());
        Mockito.when(productBranchValidations.validateProductExistsInBranch(productId, branchId)).thenReturn(Mono.empty());
        Mockito.when(productBranchPersistencePort.deleteProductFromBranch(productId, branchId)).thenReturn(Mono.empty());
        Mockito.when(productBranchPersistencePort.countProductOccurrences(productId)).thenReturn(Mono.just(0L));
        Mockito.when(productPersistencePort.deleteProduct(productId)).thenReturn(Mono.empty());

        // Act
        StepVerifier.create(productUseCase.deleteProduct(productId, branchId))
                // Assert
                .verifyComplete();

        Mockito.verify(branchValidations).validateBranchExists(branchId);
        Mockito.verify(productBranchValidations).validateProductExistsInBranch(productId, branchId);
        Mockito.verify(productBranchPersistencePort).deleteProductFromBranch(productId, branchId);
        Mockito.verify(productBranchPersistencePort).countProductOccurrences(productId);
        Mockito.verify(productPersistencePort).deleteProduct(productId);
    }

    @Test
    public void test_delete_product_from_one_branch_only() {
        // Arrange
        Long productId = 1L;
        Long branchId = 2L;

        Mockito.when(branchValidations.validateBranchExists(branchId)).thenReturn(Mono.empty());
        Mockito.when(productBranchValidations.validateProductExistsInBranch(productId, branchId)).thenReturn(Mono.empty());
        Mockito.when(productBranchPersistencePort.deleteProductFromBranch(productId, branchId)).thenReturn(Mono.empty());
        Mockito.when(productBranchPersistencePort.countProductOccurrences(productId)).thenReturn(Mono.just(1L));

        // Act
        StepVerifier.create(productUseCase.deleteProduct(productId, branchId))
                // Assert
                .verifyComplete();

        Mockito.verify(branchValidations).validateBranchExists(branchId);
        Mockito.verify(productBranchValidations).validateProductExistsInBranch(productId, branchId);
        Mockito.verify(productBranchPersistencePort).deleteProductFromBranch(productId, branchId);
        Mockito.verify(productBranchPersistencePort).countProductOccurrences(productId);
        Mockito.verify(productPersistencePort, Mockito.never()).deleteProduct(productId);
    }

    @Test
    public void test_update_stock_successfully() {
        // Arrange
        Long productId = 1L;
        Long branchId = 1L;
        int stock = 10;

        when(branchValidations.validateBranchExists(branchId)).thenReturn(Mono.empty());
        when(productBranchValidations.validateProductExistsInBranch(productId, branchId)).thenReturn(Mono.empty());
        when(productValidations.validateStock(stock)).thenReturn(Mono.empty());
        when(productBranchPersistencePort.updateStock(productId, branchId, stock)).thenReturn(Mono.empty());

        // Act
        StepVerifier.create(productUseCase.updateStock(productId, branchId, stock))
                .verifyComplete();

        // Assert
        verify(branchValidations).validateBranchExists(branchId);
        verify(productBranchValidations).validateProductExistsInBranch(productId, branchId);
        verify(productValidations).validateStock(stock);
        verify(productBranchPersistencePort).updateStock(productId, branchId, stock);
    }

    @Test
    public void test_returns_franchise_stock_model_when_franchise_exists() {
        // Arrange
        Long franchiseId = 1L;
        FranchiseStockModel expectedModel = FranchiseStockModel.builder()
                .franchiseId(franchiseId)
                .products(List.of())
                .build();


        when(franchiseValidations.validateFranchiseExists(franchiseId)).thenReturn(Mono.empty());
        when(productPersistencePort.getProductStockByFranchiseId(franchiseId)).thenReturn(Mono.just(expectedModel));

        // Act
        StepVerifier.create(productUseCase.getProductsByFranchise(franchiseId))
                // Assert
                .expectNext(expectedModel)
                .verifyComplete();

        verify(franchiseValidations).validateFranchiseExists(franchiseId);
        verify(productPersistencePort).getProductStockByFranchiseId(franchiseId);
    }

    @Test
    public void test_update_name_success() {
        // Arrange
        Long productId = 1L;
        String newName = "New Product Name";

        Mockito.when(productValidations.validateProductExistsById(productId)).thenReturn(Mono.empty());
        Mockito.when(productValidations.validateProductDoesNotExist(newName)).thenReturn(Mono.empty());
        Mockito.when(productPersistencePort.updateName(productId, newName)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.updateName(productId, newName);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(productValidations).validateProductExistsById(productId);
        Mockito.verify(productValidations).validateProductDoesNotExist(newName);
        Mockito.verify(productPersistencePort).updateName(productId, newName);
    }
}
