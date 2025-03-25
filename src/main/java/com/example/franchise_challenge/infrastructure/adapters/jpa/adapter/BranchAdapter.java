package com.example.franchise_challenge.infrastructure.adapters.jpa.adapter;

import com.example.franchise_challenge.domain.model.BranchModel;
import com.example.franchise_challenge.domain.spi.IBranchPersistencePort;
import com.example.franchise_challenge.infrastructure.adapters.jpa.entity.BranchEntity;
import com.example.franchise_challenge.infrastructure.adapters.jpa.mapper.IBranchEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.jpa.repository.IBranchRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchAdapter implements IBranchPersistencePort {

    private final IBranchRepository branchRepository;
    private final IBranchEntityMapper branchEntityMapper;

    @Override
    public Mono<Void> saveBranch(BranchModel branchModel) {
        BranchEntity entity = branchEntityMapper.toEntity(branchModel);
        return branchRepository.save(entity).then();
    }

    @Override
    public Mono<Boolean> existsBranchByName(String branchName) {
        return branchRepository.existsBranchEntitiesByName(branchName);
    }

    @Override
    public Mono<Boolean> existsBranchById(Long branchId) {
        return branchRepository.existsById(branchId);
    }

    @Override
    public Mono<Void> updateName(Long branchId, String name) {
        return branchRepository.updateName(branchId, name);
    }
}
