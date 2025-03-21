package com.example.franchise_challenge.domain.spi;

import com.example.franchise_challenge.domain.model.FranchiseModel;
import reactor.core.publisher.Mono;

public interface IFranchisePersistencePort {
    Mono<Void> saveFranchise(FranchiseModel franchiseModel);
    Mono<Boolean> existsFranchiseByName(String franchiseName);
    Mono<Boolean> existsFranchiseById(Long franchiseId);
    Mono<Void> updateName(Long franchiseId, String name);
}
