package com.example.franchise_challenge.infrastructure.entrypoints.mapper;

import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.FranchiseRequest;
import com.example.franchise_challenge.domain.model.FranchiseModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IFranchiseRequestMapper {
    FranchiseModel toModel(FranchiseRequest franchiseRequest);
}
