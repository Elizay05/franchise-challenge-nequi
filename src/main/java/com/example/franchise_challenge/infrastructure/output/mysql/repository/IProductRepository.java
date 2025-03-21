package com.example.franchise_challenge.infrastructure.output.mysql.repository;

import com.example.franchise_challenge.infrastructure.output.mysql.dto.ProductStockDTO;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.ProductEntity;
import com.example.franchise_challenge.infrastructure.output.mysql.utils.constants.OutputConstants;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductRepository extends R2dbcRepository<ProductEntity, Long> {
    Mono<Boolean> existsProductEntitiesByName(String name);

    Mono<Void> deleteById(Long productId);

    @Query(OutputConstants.QUERY_TO_GET_MOST_STOCKED_PRODUCTS_BY_FRANCHISE_ID)
    Flux<ProductStockDTO> findTopStockedProductsByFranchiseId(Long franchiseId);

    @Query(OutputConstants.QUERY_TO_UPDATE_PRODUCT_NAME)
    Mono<Void> updateName(Long productId, String name);
}
