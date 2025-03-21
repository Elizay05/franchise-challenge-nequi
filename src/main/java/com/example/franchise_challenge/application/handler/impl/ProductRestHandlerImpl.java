package com.example.franchise_challenge.application.handler.impl;

import com.example.franchise_challenge.application.dto.request.ProductRequest;
import com.example.franchise_challenge.application.dto.response.FranchiseStockResponse;
import com.example.franchise_challenge.application.handler.IProductRestHandler;
import com.example.franchise_challenge.application.mapper.IProductRequestMapper;
import com.example.franchise_challenge.application.mapper.IFranchiseStockResponseMapper;
import com.example.franchise_challenge.domain.api.IProductServicePort;
import com.example.franchise_challenge.domain.model.ProductModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductRestHandlerImpl implements IProductRestHandler {

    private final IProductServicePort productServicePort;
    private final IProductRequestMapper productRequestMapper;
    private final IFranchiseStockResponseMapper franchiseStockResponseMapper;

    @Override
    public Mono<Void> createProduct(ProductRequest productRequest) {
        ProductModel productModel = productRequestMapper.toModel(productRequest);
        return productServicePort.createProduct(productModel);
    }

    @Override
    public Mono<Void> deleteProduct(Long productId, Long branchId) {
        return productServicePort.deleteProduct(productId, branchId);
    }

    @Override
    public Mono<Void> updateStock(Long productId, Long branchId, int stock) {
        return productServicePort.updateStock(productId, branchId, stock);
    }

    @Override
    public Mono<FranchiseStockResponse> getAllProductStockByFranchise(Long franchiseId) {
        return productServicePort.getProductsByFranchise(franchiseId)
                .map(franchiseStockResponseMapper::toResponse);
    }

    @Override
    public Mono<Void> updateName(Long productId, String name) {
        return productServicePort.updateName(productId, name);
    }
}
