package com.example.franchise_challenge.domain.spi;

import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import reactor.core.publisher.Mono;

public interface IProductPersistencePort {
    Mono<Void> saveProduct(ProductModel productModel);
    Mono<Boolean> existsProductByName(String productName);
    Mono<Boolean> productExistsInBranch(Long productId, Long branchId);
    Mono<Void> deleteProductFromBranch(Long productId, Long branchId);
    Mono<Void> updateStock(Long productId, Long branchId, int stock);
    Mono<FranchiseStockModel> getProductStockByFranchiseId(Long franchiseId);
    Mono<Void> updateName(Long productId, String name);
    Mono<Boolean> existsProductById(Long productId);
}
