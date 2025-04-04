package com.example.franchise_challenge.infrastructure.adapters.jpa.mapper;

import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.infrastructure.adapters.jpa.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IProductEntityMapper {
    ProductEntity toEntity(ProductModel productModel);
    ProductModel toModel(ProductEntity productEntity);
}
