package com.example.franchise_challenge.domain.usecase;

import com.example.franchise_challenge.domain.api.IProductServicePort;
import com.example.franchise_challenge.domain.model.ProductBranchModel;
import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.spi.IProductBranchPersistencePort;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.domain.utils.constants.DomainConstants;
import com.example.franchise_challenge.domain.validationsUseCase.BranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.FranchiseValidations;
import com.example.franchise_challenge.domain.validationsUseCase.ProductBranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.ProductValidations;
import reactor.core.publisher.Mono;

public class ProductUseCase implements IProductServicePort {

    private final IProductPersistencePort productPersistencePort;
    private final IProductBranchPersistencePort productBranchPersistencePort;
    private final ProductValidations productValidations;
    private final BranchValidations branchValidations;
    private final FranchiseValidations franchiseValidations;
    private final ProductBranchValidations productBranchValidations;


    public ProductUseCase(IProductPersistencePort productPersistencePort,
                          IProductBranchPersistencePort productBranchPersistencePort,
                          ProductValidations productValidations,
                          BranchValidations branchValidations,
                          FranchiseValidations franchiseValidations,
                          ProductBranchValidations productBranchValidations) {
        this.productPersistencePort = productPersistencePort;
        this.productBranchPersistencePort = productBranchPersistencePort;
        this.productValidations = productValidations;
        this.branchValidations = branchValidations;
        this.franchiseValidations = franchiseValidations;
        this.productBranchValidations = productBranchValidations;
    }

    @Override
    public Mono<Void> createProduct(ProductModel productModel) {
        return Mono.when(
                        branchValidations.validateBranchExists(productModel.getBranchId()),
                        productValidations.validateProductDoesNotExist(productModel.getName()),
                        productValidations.validateStock(productModel.getStock())
                )
                .then(productPersistencePort.saveProduct(productModel))
                .flatMap(savedProduct -> {
                    ProductBranchModel productBranchModel =new ProductBranchModel(
                            null,
                            savedProduct.getId(),
                            productModel.getBranchId(),
                            productModel.getStock()
                    );
                    return productBranchPersistencePort.saveProductBranch(productBranchModel);
                })
                .then();
    }

    @Override
    public Mono<Void> deleteProduct(Long productId, Long branchId) {
        return Mono.when(
                        branchValidations.validateBranchExists(branchId),
                        productBranchValidations.validateProductExistsInBranch(productId, branchId)
                ).then(productBranchPersistencePort.deleteProductFromBranch(productId, branchId))
                .then(productBranchPersistencePort.countProductOccurrences(productId))
                .flatMap(count -> count == DomainConstants.ZERO ? productPersistencePort.deleteProduct(productId) : Mono.empty());
    }

    @Override
    public Mono<Void> updateStock(Long productId, Long branchId, int stock) {
        return Mono.when(
                branchValidations.validateBranchExists(branchId),
                productBranchValidations.validateProductExistsInBranch(productId, branchId),
                productValidations.validateStock(stock)
        ).then(productBranchPersistencePort.updateStock(productId, branchId, stock));
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
