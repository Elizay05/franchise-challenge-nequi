package com.example.franchise_challenge.domain.usecase;

import com.example.franchise_challenge.domain.api.IProductServicePort;
import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.domain.validationsUseCase.BranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.FranchiseValidations;
import com.example.franchise_challenge.domain.validationsUseCase.ProductValidations;
import reactor.core.publisher.Mono;

public class ProductUseCase implements IProductServicePort {

    private final IProductPersistencePort productPersistencePort;
    private final ProductValidations productValidations;
    private final BranchValidations branchValidations;
    private final FranchiseValidations franchiseValidations;


    public ProductUseCase(IProductPersistencePort productPersistencePort,
                          ProductValidations productValidations,
                          BranchValidations branchValidations,
                          FranchiseValidations franchiseValidations) {
        this.productPersistencePort = productPersistencePort;
        this.productValidations = productValidations;
        this.branchValidations = branchValidations;
        this.franchiseValidations = franchiseValidations;
    }

    @Override
    public Mono<Void> createProduct(ProductModel productModel) {
        return Mono.when(
                        branchValidations.validateBranchExists(productModel.getBranchId()),
                        productValidations.validateProductDoesNotExist(productModel.getName()),
                        productValidations.validateStock(productModel.getStock())
                )
                .then(productPersistencePort.saveProduct(productModel));
    }

    @Override
    public Mono<Void> deleteProduct(Long productId, Long branchId) {
        return Mono.when(
                branchValidations.validateBranchExists(branchId),
                productValidations.validateProductExistsInBranch(productId, branchId)
        ).then(productPersistencePort.deleteProductFromBranch(productId, branchId));
    }

    @Override
    public Mono<Void> updateStock(Long productId, Long branchId, int stock) {
        return Mono.when(
                branchValidations.validateBranchExists(branchId),
                productValidations.validateProductExistsInBranch(productId, branchId),
                productValidations.validateStock(stock)
        ).then(productPersistencePort.updateStock(productId, branchId, stock));
    }

    @Override
    public Mono<FranchiseStockModel> getProductsByFranchise(Long franchiseId) {
        return Mono.when(
                franchiseValidations.validateFranchiseExists(franchiseId)
        ).then(productPersistencePort.getProductStockByFranchiseId(franchiseId));
    }

    @Override
    public Mono<Void> updateName(Long productId, String name) {
        return Mono.when(
                productValidations.validateProductExistsById(productId),
                productValidations.validateProductDoesNotExist(name)
        ).then(productPersistencePort.updateName(productId, name));
    }
}
