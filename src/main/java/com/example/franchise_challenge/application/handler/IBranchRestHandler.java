package com.example.franchise_challenge.application.handler;

import com.example.franchise_challenge.application.dto.request.BranchRequest;
import reactor.core.publisher.Mono;

public interface IBranchRestHandler {
    Mono<Void> createBranch(BranchRequest branchRequest);
    Mono<Void> updateName(Long branchId, String name);
}
