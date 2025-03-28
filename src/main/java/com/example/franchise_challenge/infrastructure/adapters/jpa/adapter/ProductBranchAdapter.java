package com.example.franchise_challenge.infrastructure.adapters.jpa.adapter;

import com.example.franchise_challenge.domain.model.ProductBranchModel;
import com.example.franchise_challenge.domain.spi.IProductBranchPersistencePort;
import com.example.franchise_challenge.infrastructure.adapters.jpa.entity.ProductBranchEntity;
import com.example.franchise_challenge.infrastructure.adapters.jpa.mapper.IProductBranchEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.jpa.repository.IProductBranchRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductBranchAdapter implements IProductBranchPersistencePort {

    private final IProductBranchRepository productBranchRepository;
    private final IProductBranchEntityMapper productBranchEntityMapper;

    @Override
    public Mono<Void> saveProductBranch(ProductBranchModel productBranchModel) {
        ProductBranchEntity entity = productBranchEntityMapper.toEntity(productBranchModel);
        return productBranchRepository.save(entity).then();
    }

    @Override
    public Mono<Boolean> productExistsInBranch(Long productId, Long branchId) {
        return productBranchRepository.existsByProductIdAndBranchId(productId, branchId);
    }

    @Override
    public Mono<Void> deleteProductFromBranch(Long productId, Long branchId) {
        return productBranchRepository.deleteByProductIdAndBranchId(productId, branchId);
    }

    @Override
    public Mono<Long> countProductOccurrences(Long productId) {
        return productBranchRepository.countByProductId(productId);
    }

    @Override
    public Mono<Void> updateStock(Long productId, Long branchId, int stock) {
        return productBranchRepository.updateStock(productId, branchId, stock);
    }
}
