package com.example.franchise_challenge.application.handler.impl;

import com.example.franchise_challenge.application.dto.request.FranchiseRequest;
import com.example.franchise_challenge.application.handler.IFranchiseRestHandler;
import com.example.franchise_challenge.application.mapper.IFranchiseRequestMapper;
import com.example.franchise_challenge.domain.api.IFranchiseServicePort;
import com.example.franchise_challenge.domain.model.FranchiseModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseRestHandlerImpl implements IFranchiseRestHandler {

    private final IFranchiseServicePort franchiseServicePort;
    private final IFranchiseRequestMapper franchiseRequestMapper;

    @Override
    public Mono<Void> createFranchise(FranchiseRequest franchiseRequest) {
        FranchiseModel franchiseModel = franchiseRequestMapper.toModel(franchiseRequest);
        return franchiseServicePort.createFranchise(franchiseModel);
    }

    @Override
    public Mono<Void> updateName(Long franchiseId, String name) {
        return franchiseServicePort.updateName(franchiseId, name);
    }
}
