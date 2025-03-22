package com.example.franchise_challenge.infrastructure.entrypoints.mapper;

import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.ProductRequest;
import com.example.franchise_challenge.domain.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IProductRequestMapper {
    ProductModel toModel(ProductRequest productRequest);
}
