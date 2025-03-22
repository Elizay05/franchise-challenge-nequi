package com.example.franchise_challenge.domain.validationsUseCase;

import com.example.franchise_challenge.domain.exceptions.InvalidStockException;
import com.example.franchise_challenge.domain.exceptions.ProductAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.ProductNotFoundException;
import com.example.franchise_challenge.domain.exceptions.ProductNotFoundInBranchException;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.domain.utils.constants.DomainConstants;
import reactor.core.publisher.Mono;

public class ProductValidations {

    private final IProductPersistencePort productPersistencePort;

    public ProductValidations(IProductPersistencePort productPersistencePort) {
        this.productPersistencePort = productPersistencePort;
    }

    public Mono<Void> validateProductDoesNotExist(String productName) {
        return productPersistencePort.existsProductByName(productName)
                .flatMap(exists -> exists
                        ? Mono.error(new ProductAlreadyExistsException(DomainConstants.PRODUCT_ALREADY_EXISTS))
                        : Mono.empty());
    }

    public Mono<Void> validateStock(int stock) {
        return stock > DomainConstants.MIN_STOCK
                ? Mono.empty()
                : Mono.error(new InvalidStockException(DomainConstants.INVALID_STOCK));
    }

    public Mono<Void> validateProductExistsById(Long productId) {
        return productPersistencePort.existsProductById(productId)
                .flatMap(exists -> exists
                        ? Mono.empty()
                        : Mono.error(new ProductNotFoundException(String.format(DomainConstants.PRODUCT_NOT_FOUND, productId))));
    }
}
