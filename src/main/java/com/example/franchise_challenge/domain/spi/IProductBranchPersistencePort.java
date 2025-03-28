package com.example.franchise_challenge.domain.spi;

import com.example.franchise_challenge.domain.model.ProductBranchModel;
import reactor.core.publisher.Mono;

public interface IProductBranchPersistencePort {
    Mono<Void> saveProductBranch(ProductBranchModel productBranchModel);
    Mono<Boolean> productExistsInBranch(Long productId, Long branchId);
    Mono<Void> deleteProductFromBranch(Long productId, Long branchId);
    Mono<Long> countProductOccurrences(Long productId);
    Mono<Void> updateStock(Long productId, Long branchId, int stock);
}
