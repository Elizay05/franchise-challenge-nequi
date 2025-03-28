package com.example.franchise_challenge.domain.spi;

import com.example.franchise_challenge.domain.model.BranchModel;
import reactor.core.publisher.Mono;

public interface IBranchPersistencePort {
    Mono<Void> saveBranch(BranchModel branchModel);
    Mono<Boolean> existsBranchByName(String branchName);
    Mono<Boolean> existsBranchById(Long branchId);
    Mono<Void> updateName(Long branchId, String name);
}
