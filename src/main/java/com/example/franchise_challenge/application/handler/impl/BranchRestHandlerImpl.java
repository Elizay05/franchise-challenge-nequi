package com.example.franchise_challenge.application.handler.impl;

import com.example.franchise_challenge.application.dto.request.BranchRequest;
import com.example.franchise_challenge.application.handler.IBranchRestHandler;
import com.example.franchise_challenge.application.mapper.IBranchRequestMapper;
import com.example.franchise_challenge.domain.api.IBranchServicePort;
import com.example.franchise_challenge.domain.model.BranchModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchRestHandlerImpl implements IBranchRestHandler {

    private final IBranchServicePort branchServicePort;
    private final IBranchRequestMapper branchRequestMapper;

    @Override
    public Mono<Void> createBranch(BranchRequest branchRequest) {
        BranchModel branchModel = branchRequestMapper.toModel(branchRequest);
        return branchServicePort.createBranch(branchModel);
    }

    @Override
    public Mono<Void> updateName(Long branchId, String name) {
        return branchServicePort.updateName(branchId, name);
    }
}
