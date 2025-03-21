package com.example.franchise_challenge.application.handler;

import com.example.franchise_challenge.application.dto.request.ProductRequest;
import com.example.franchise_challenge.application.dto.response.FranchiseStockResponse;
import reactor.core.publisher.Mono;

public interface IProductRestHandler {
    Mono<Void> createProduct(ProductRequest productRequest);
    Mono<Void> deleteProduct(Long productId, Long branchId);
    Mono<Void> updateStock(Long productId, Long branchId, int stock);
    Mono<FranchiseStockResponse> getAllProductStockByFranchise(Long franchiseId);
    Mono<Void> updateName(Long productId, String name);
}
