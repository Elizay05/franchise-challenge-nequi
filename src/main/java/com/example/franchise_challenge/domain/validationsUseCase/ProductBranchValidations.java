package com.example.franchise_challenge.domain.validationsUseCase;

import com.example.franchise_challenge.domain.exceptions.ProductNotFoundInBranchException;
import com.example.franchise_challenge.domain.spi.IProductBranchPersistencePort;
import com.example.franchise_challenge.domain.utils.constants.DomainConstants;
import reactor.core.publisher.Mono;

public class ProductBranchValidations {

    private final IProductBranchPersistencePort productBranchPersistencePort;

    public ProductBranchValidations(IProductBranchPersistencePort productBranchPersistencePort) {
        this.productBranchPersistencePort = productBranchPersistencePort;
    }

    public Mono<Void> validateProductExistsInBranch(Long productId, Long branchId) {
        return productBranchPersistencePort.productExistsInBranch(productId, branchId)
                .flatMap(exists -> exists
                        ? Mono.empty()
                        : Mono.error(new ProductNotFoundInBranchException(DomainConstants.PRODUCT_NOT_FOUND_IN_BRANCH)));
    }
}
