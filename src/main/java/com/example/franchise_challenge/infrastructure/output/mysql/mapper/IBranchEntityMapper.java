package com.example.franchise_challenge.infrastructure.output.mysql.mapper;

import com.example.franchise_challenge.domain.model.BranchModel;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.BranchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBranchEntityMapper {
    BranchEntity toEntity(BranchModel branchModel);
}
