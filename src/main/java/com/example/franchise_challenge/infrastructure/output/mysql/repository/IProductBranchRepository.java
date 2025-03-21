package com.example.franchise_challenge.infrastructure.output.mysql.repository;

import com.example.franchise_challenge.infrastructure.output.mysql.entity.ProductBranchEntity;
import com.example.franchise_challenge.infrastructure.output.mysql.utils.constants.OutputConstants;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface IProductBranchRepository extends R2dbcRepository<ProductBranchEntity, Long> {
    Mono<Boolean> existsByProductIdAndBranchId(Long productId, Long branchId);

    Mono<Void> deleteByProductIdAndBranchId(Long productId, Long branchId);

    Mono<Long> countByProductId(Long productId);

    @Modifying
    @Query(OutputConstants.QUERY_TO_UPDATE_STOCK)
    Mono<Void> updateStock(@Param(OutputConstants.PARAM_QUERY_TO_UPDATE_STOCK_PRODUCT_ID) Long productId,
                           @Param(OutputConstants.PARAM_QUERY_TO_UPDATE_STOCK_BRANCH_ID) Long branchId,
                           @Param(OutputConstants.PARAM_QUERY_TO_UPDATE_STOCK_STOCK) int stock);
}
