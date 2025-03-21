package com.example.franchise_challenge.application.mapper;

import com.example.franchise_challenge.application.dto.request.BranchRequest;
import com.example.franchise_challenge.domain.model.BranchModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBranchRequestMapper {
    BranchModel toModel(BranchRequest branchRequest);
}
