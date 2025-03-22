package com.example.franchise_challenge.infrastructure.adapters.mysql.mapper;

import com.example.franchise_challenge.domain.model.ProductBranchModel;
import com.example.franchise_challenge.infrastructure.adapters.mysql.entity.ProductBranchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IProductBranchEntityMapper {
    ProductBranchEntity toEntity(ProductBranchModel productBranchModel);
    ProductBranchModel toModel(ProductBranchEntity productBranchEntity);
}
