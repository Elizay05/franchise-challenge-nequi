package com.example.franchise_challenge.infrastructure.entrypoints.mapper;

import com.example.franchise_challenge.infrastructure.entrypoints.dto.response.FranchiseStockResponse;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IFranchiseStockResponseMapper {
    FranchiseStockResponse toResponse(FranchiseStockModel model);
}
