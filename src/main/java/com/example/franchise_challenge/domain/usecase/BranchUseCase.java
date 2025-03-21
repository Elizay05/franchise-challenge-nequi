package com.example.franchise_challenge.domain.usecase;

import com.example.franchise_challenge.domain.api.IBranchServicePort;
import com.example.franchise_challenge.domain.model.BranchModel;
import com.example.franchise_challenge.domain.spi.IBranchPersistencePort;
import com.example.franchise_challenge.domain.validationsUseCase.BranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.FranchiseValidations;
import reactor.core.publisher.Mono;

public class BranchUseCase implements IBranchServicePort {

    private final IBranchPersistencePort branchPersistencePort;
    private final FranchiseValidations franchiseValidations;
    private final BranchValidations branchValidations;

    public BranchUseCase(IBranchPersistencePort branchPersistencePort, FranchiseValidations franchiseValidations, BranchValidations branchValidations) {
        this.branchPersistencePort = branchPersistencePort;
        this.franchiseValidations = franchiseValidations;
        this.branchValidations = branchValidations;
    }

    @Override
    public Mono<Void> createBranch(BranchModel branchModel) {
        return Mono.when(
                        franchiseValidations.validateFranchiseExists(branchModel.getFranchiseId()),
                        branchValidations.validateBranchDoesNotExist(branchModel.getName())
                )
                .then(branchPersistencePort.saveBranch(branchModel));
    }

    @Override
    public Mono<Void> updateName(Long branchId, String name) {
        return Mono.when(
                branchValidations.validateBranchExists(branchId),
                branchValidations.validateBranchDoesNotExist(name)
        ).then(branchPersistencePort.updateName(branchId, name));
    }
}
