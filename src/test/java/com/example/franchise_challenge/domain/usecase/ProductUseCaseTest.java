package com.example.franchise_challenge.domain.usecase;

import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.domain.validationsUseCase.BranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.FranchiseValidations;
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
    private BranchValidations branchValidations;

    @Mock
    private ProductValidations productValidations;

    @Mock
    private FranchiseValidations franchiseValidations;

    @InjectMocks
    private ProductUseCase productUseCase;

    @Test
    public void test_create_product_success_when_all_validations_pass() {
        // Arrange

        ProductModel productModel = new ProductModel(1L, "Test Product", 1L, 10);

        when(branchValidations.validateBranchExists(productModel.getBranchId())).thenReturn(Mono.empty());
        when(productValidations.validateProductDoesNotExist(productModel.getName())).thenReturn(Mono.empty());
        when(productValidations.validateStock(productModel.getStock())).thenReturn(Mono.empty());
        when(productPersistencePort.saveProduct(productModel)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.createProduct(productModel);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(branchValidations).validateBranchExists(productModel.getBranchId());
        verify(productValidations).validateProductDoesNotExist(productModel.getName());
        verify(productValidations).validateStock(productModel.getStock());
        verify(productPersistencePort).saveProduct(productModel);
    }

    @Test
    public void test_delete_product_successfully() {
        // Arrange
        Long productId = 1L;
        Long branchId = 1L;

        Mockito.when(branchValidations.validateBranchExists(branchId)).thenReturn(Mono.empty());
        Mockito.when(productValidations.validateProductExistsInBranch(productId, branchId)).thenReturn(Mono.empty());
        Mockito.when(productPersistencePort.deleteProductFromBranch(productId, branchId)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = productUseCase.deleteProduct(productId, branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(branchValidations).validateBranchExists(branchId);
        Mockito.verify(productValidations).validateProductExistsInBranch(productId, branchId);
        Mockito.verify(productPersistencePort).deleteProductFromBranch(productId, branchId);
    }

    @Test
    public void test_update_stock_success() {
        // Arrange
        Long productId = 1L;
        Long branchId = 1L;
        int stock = 10;

        Mockito.when(branchValidations.validateBranchExists(branchId)).thenReturn(Mono.empty());
        Mockito.when(productValidations.validateProductExistsInBranch(productId, branchId)).thenReturn(Mono.empty());
        Mockito.when(productValidations.validateStock(stock)).thenReturn(Mono.empty());
        Mockito.when(productPersistencePort.updateStock(productId, branchId, stock)).thenReturn(Mono.empty());

        // Act
        StepVerifier.create(productUseCase.updateStock(productId, branchId, stock))
                .verifyComplete();

        // Assert
        Mockito.verify(branchValidations).validateBranchExists(branchId);
        Mockito.verify(productValidations).validateProductExistsInBranch(productId, branchId);
        Mockito.verify(productValidations).validateStock(stock);
        Mockito.verify(productPersistencePort).updateStock(productId, branchId, stock);
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
