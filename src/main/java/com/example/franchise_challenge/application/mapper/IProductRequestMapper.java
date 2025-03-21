package com.example.franchise_challenge.application.mapper;

import com.example.franchise_challenge.application.dto.request.ProductRequest;
import com.example.franchise_challenge.domain.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IProductRequestMapper {
    ProductModel toModel(ProductRequest productRequest);
}
