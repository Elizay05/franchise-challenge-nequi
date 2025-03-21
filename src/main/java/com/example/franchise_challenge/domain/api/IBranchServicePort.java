package com.example.franchise_challenge.domain.api;

import com.example.franchise_challenge.domain.model.BranchModel;
import reactor.core.publisher.Mono;

public interface IBranchServicePort {
    Mono<Void> createBranch(BranchModel branchModel);
    Mono<Void> updateName(Long branchId, String name);
}
