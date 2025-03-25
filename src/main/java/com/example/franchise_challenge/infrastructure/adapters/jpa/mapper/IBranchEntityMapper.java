package com.example.franchise_challenge.infrastructure.adapters.jpa.mapper;

import com.example.franchise_challenge.domain.model.BranchModel;
import com.example.franchise_challenge.infrastructure.adapters.jpa.entity.BranchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBranchEntityMapper {
    BranchEntity toEntity(BranchModel branchModel);
}
