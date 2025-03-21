package com.example.franchise_challenge.domain.usecase;

import com.example.franchise_challenge.domain.api.IFranchiseServicePort;
import com.example.franchise_challenge.domain.model.FranchiseModel;
import com.example.franchise_challenge.domain.spi.IFranchisePersistencePort;
import com.example.franchise_challenge.domain.validationsUseCase.FranchiseValidations;
import reactor.core.publisher.Mono;

public class FranchiseUseCase implements IFranchiseServicePort {

    private final IFranchisePersistencePort franchisePersistencePort;
    private final FranchiseValidations franchiseValidations;

    public FranchiseUseCase(IFranchisePersistencePort franchisePersistencePort, FranchiseValidations franchiseValidations) {
        this.franchisePersistencePort = franchisePersistencePort;
        this.franchiseValidations = franchiseValidations;
    }

    @Override
    public Mono<Void> createFranchise(FranchiseModel franchiseModel) {
        return Mono.when(
                franchiseValidations.validateFranchiseDoesNotExist(franchiseModel.getName())
        ).then(franchisePersistencePort.saveFranchise(franchiseModel));
    }

    @Override
    public Mono<Void> updateName(Long franchiseId, String name) {
        return Mono.when(
                franchiseValidations.validateFranchiseExists(franchiseId),
                franchiseValidations.validateFranchiseDoesNotExist(name)
        ).then(franchisePersistencePort.updateName(franchiseId, name));
    }
}
