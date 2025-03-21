package com.example.franchise_challenge.domain.validationsUseCase;

import com.example.franchise_challenge.domain.exceptions.BranchAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.BranchNotFoundException;
import com.example.franchise_challenge.domain.spi.IBranchPersistencePort;
import com.example.franchise_challenge.domain.utils.constants.DomainConstants;
import reactor.core.publisher.Mono;

public class BranchValidations {

    private final IBranchPersistencePort branchPersistencePort;

    public BranchValidations(IBranchPersistencePort branchPersistencePort) {
        this.branchPersistencePort = branchPersistencePort;
    }

    public Mono<Void> validateBranchExists(Long branchId) {
        return branchPersistencePort.existsBranchById(branchId)
                .flatMap(exists -> exists
                        ? Mono.empty()
                        : Mono.error(new BranchNotFoundException(
                        String.format(DomainConstants.BRANCH_NOT_FOUND, branchId))));
    }

    public Mono<Void> validateBranchDoesNotExist(String branchName) {
        return branchPersistencePort.existsBranchByName(branchName)
                .flatMap(exists -> exists
                        ? Mono.error(new BranchAlreadyExistsException(DomainConstants.BRANCH_ALREADY_EXISTS))
                        : Mono.empty());
    }
}
