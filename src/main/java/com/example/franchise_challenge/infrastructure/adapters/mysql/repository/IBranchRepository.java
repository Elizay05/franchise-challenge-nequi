package com.example.franchise_challenge.infrastructure.adapters.mysql.repository;

import com.example.franchise_challenge.infrastructure.adapters.mysql.entity.BranchEntity;
import com.example.franchise_challenge.infrastructure.adapters.mysql.utils.constants.OutputConstants;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface IBranchRepository extends R2dbcRepository<BranchEntity, Long> {
    Mono<Boolean> existsBranchEntitiesByName(String name);

    @Query(OutputConstants.QUERY_TO_UPDATE_BRANCH_NAME)
    Mono<Void> updateName(Long branchId, String name);
}
