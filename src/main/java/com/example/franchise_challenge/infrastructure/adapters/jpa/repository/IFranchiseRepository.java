package com.example.franchise_challenge.infrastructure.adapters.jpa.repository;

import com.example.franchise_challenge.infrastructure.adapters.jpa.entity.FranchiseEntity;
import com.example.franchise_challenge.infrastructure.adapters.jpa.utils.constants.OutputConstants;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface IFranchiseRepository extends R2dbcRepository<FranchiseEntity, Long> {
    Mono<Boolean> existsFranchiseEntitiesByName(String name);

    @Query(OutputConstants.QUERY_TO_UPDATE_FRANCHISE_NAME)
    Mono<Void> updateName(Long franchiseId, String name);
}
