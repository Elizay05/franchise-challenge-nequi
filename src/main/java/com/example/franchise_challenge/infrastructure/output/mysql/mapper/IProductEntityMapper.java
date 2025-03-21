package com.example.franchise_challenge.infrastructure.output.mysql.mapper;

import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IProductEntityMapper {
    ProductEntity toEntity(ProductModel productModel);
    ProductModel toModel(ProductEntity productEntity);
}
