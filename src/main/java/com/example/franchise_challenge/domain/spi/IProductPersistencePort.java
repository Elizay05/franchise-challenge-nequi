package com.example.franchise_challenge.domain.spi;

import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import reactor.core.publisher.Mono;

public interface IProductPersistencePort {
    Mono<ProductModel> saveProduct(ProductModel productModel);
    Mono<Boolean> existsProductByName(String productName);
    Mono<Void> deleteProduct(Long productId);
    Mono<FranchiseStockModel> getProductStockByFranchiseId(Long franchiseId);
    Mono<Void> updateName(Long productId, String name);
    Mono<Boolean> existsProductById(Long productId);
}
