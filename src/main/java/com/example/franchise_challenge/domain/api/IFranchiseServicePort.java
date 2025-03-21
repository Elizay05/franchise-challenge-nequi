package com.example.franchise_challenge.domain.api;

import com.example.franchise_challenge.domain.model.FranchiseModel;
import reactor.core.publisher.Mono;

public interface IFranchiseServicePort {
    Mono<Void> createFranchise(FranchiseModel franchiseModel);
    Mono<Void> updateName(Long franchiseId, String name);
}
