package com.example.franchise_challenge.infrastructure.output.mysql.adapter;

import com.example.franchise_challenge.domain.model.FranchiseModel;
import com.example.franchise_challenge.domain.spi.IFranchisePersistencePort;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.FranchiseEntity;
import com.example.franchise_challenge.infrastructure.output.mysql.mapper.IFranchiseEntityMapper;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseAdapter implements IFranchisePersistencePort {

    private final IFranchiseRepository franchiseRepository;
    private final IFranchiseEntityMapper franchiseEntityMapper;

    @Override
    public Mono<Void> saveFranchise(FranchiseModel franchiseModel) {
        FranchiseEntity entity = franchiseEntityMapper.toEntity(franchiseModel);
        return franchiseRepository.save(entity).then();
    }

    @Override
    public Mono<Boolean> existsFranchiseByName(String franchiseName) {
        return franchiseRepository.existsFranchiseEntitiesByName(franchiseName);
    }

    @Override
    public Mono<Boolean> existsFranchiseById(Long franchiseId) {
        return franchiseRepository.existsById(franchiseId);
    }

    @Override
    public Mono<Void> updateName(Long franchiseId, String name) {
        return franchiseRepository.updateName(franchiseId, name);
    }
}
