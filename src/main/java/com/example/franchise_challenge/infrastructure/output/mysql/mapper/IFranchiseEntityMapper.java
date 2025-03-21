package com.example.franchise_challenge.infrastructure.output.mysql.mapper;

import com.example.franchise_challenge.domain.model.FranchiseModel;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.FranchiseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IFranchiseEntityMapper {
    FranchiseEntity toEntity(FranchiseModel franchiseModel);
}
