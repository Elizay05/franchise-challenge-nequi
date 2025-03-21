package com.example.franchise_challenge.application.handler;

import com.example.franchise_challenge.application.dto.request.FranchiseRequest;
import reactor.core.publisher.Mono;

public interface IFranchiseRestHandler {
    Mono<Void> createFranchise(FranchiseRequest franchiseRequest);
    Mono<Void> updateName(Long franchiseId, String name);
}
