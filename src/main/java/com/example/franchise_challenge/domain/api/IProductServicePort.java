package com.example.franchise_challenge.domain.api;

import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import reactor.core.publisher.Mono;

public interface IProductServicePort {
    Mono<Void> createProduct(ProductModel productModel);
    Mono<Void> deleteProduct(Long productId, Long branchId);
    Mono<Void> updateStock(Long productId, Long branchId, int stock);
    Mono<FranchiseStockModel> getProductsByFranchise(Long franchiseId);
    Mono<Void> updateName(Long productId, String name);
}
