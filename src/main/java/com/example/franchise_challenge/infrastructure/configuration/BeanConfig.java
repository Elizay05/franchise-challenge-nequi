package com.example.franchise_challenge.infrastructure.configuration;

import com.example.franchise_challenge.application.handler.IBranchRestHandler;
import com.example.franchise_challenge.application.handler.IFranchiseRestHandler;
import com.example.franchise_challenge.application.handler.IProductRestHandler;
import com.example.franchise_challenge.application.handler.impl.BranchRestHandlerImpl;
import com.example.franchise_challenge.application.handler.impl.FranchiseRestHandlerImpl;
import com.example.franchise_challenge.application.handler.impl.ProductRestHandlerImpl;
import com.example.franchise_challenge.application.mapper.IBranchRequestMapper;
import com.example.franchise_challenge.application.mapper.IFranchiseRequestMapper;
import com.example.franchise_challenge.application.mapper.IFranchiseStockResponseMapper;
import com.example.franchise_challenge.application.mapper.IProductRequestMapper;
import com.example.franchise_challenge.domain.api.IBranchServicePort;
import com.example.franchise_challenge.domain.api.IFranchiseServicePort;
import com.example.franchise_challenge.domain.api.IProductServicePort;
import com.example.franchise_challenge.domain.spi.IBranchPersistencePort;
import com.example.franchise_challenge.domain.spi.IFranchisePersistencePort;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.domain.usecase.BranchUseCase;
import com.example.franchise_challenge.domain.usecase.FranchiseUseCase;
import com.example.franchise_challenge.domain.usecase.ProductUseCase;
import com.example.franchise_challenge.domain.validationsUseCase.BranchValidations;
import com.example.franchise_challenge.domain.validationsUseCase.FranchiseValidations;
import com.example.franchise_challenge.domain.validationsUseCase.ProductValidations;
import com.example.franchise_challenge.infrastructure.output.mysql.adapter.BranchAdapter;
import com.example.franchise_challenge.infrastructure.output.mysql.adapter.FranchiseAdapter;
import com.example.franchise_challenge.infrastructure.output.mysql.adapter.ProductAdapter;
import com.example.franchise_challenge.infrastructure.output.mysql.mapper.IBranchEntityMapper;
import com.example.franchise_challenge.infrastructure.output.mysql.mapper.IFranchiseEntityMapper;
import com.example.franchise_challenge.infrastructure.output.mysql.mapper.IProductEntityMapper;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IBranchRepository;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IFranchiseRepository;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IProductBranchRepository;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final IFranchiseRepository franchiseRepository;
    private final IFranchiseEntityMapper franchiseEntityMapper;
    private final IFranchiseRequestMapper franchiseRequestMapper;
    private final IBranchRepository branchRepository;
    private final IBranchEntityMapper branchEntityMapper;
    private final IBranchRequestMapper branchRequestMapper;
    private final IProductRepository productRepository;
    private final IProductBranchRepository productBranchRepository;
    private final IProductEntityMapper productEntityMapper;
    private final IProductRequestMapper productRequestMapper;
    private final IFranchiseStockResponseMapper franchiseStockResponseMapper;

    @Bean
    public IFranchiseServicePort franchiseServicePort() {
        return new FranchiseUseCase(franchisePersistencePort(), franchiseValidations());
    }

    @Bean
    public IFranchisePersistencePort franchisePersistencePort() {
        return new FranchiseAdapter(franchiseRepository, franchiseEntityMapper);
    }

    @Bean
    public FranchiseValidations franchiseValidations() {
        return new FranchiseValidations(franchisePersistencePort());
    }

    @Bean
    public IFranchiseRestHandler franchiseRestHandler() {
        return new FranchiseRestHandlerImpl(franchiseServicePort(), franchiseRequestMapper);
    }

    @Bean
    public IBranchServicePort branchServicePort() {
        return new BranchUseCase(branchPersistencePort(), franchiseValidations(), branchValidations());
    }

    @Bean
    public IBranchPersistencePort branchPersistencePort() {
        return new BranchAdapter(branchRepository, branchEntityMapper);
    }

    @Bean
    public BranchValidations branchValidations() {
        return new BranchValidations(branchPersistencePort());
    }

    @Bean
    public IBranchRestHandler branchRestHandler() {
        return new BranchRestHandlerImpl(branchServicePort(), branchRequestMapper);
    }

    @Bean
    public IProductServicePort productServicePort() {
        return new ProductUseCase(productPersistencePort(), productValidations(), branchValidations(), franchiseValidations());
    }

    @Bean
    public IProductPersistencePort productPersistencePort() {
        return new ProductAdapter(productRepository, productBranchRepository, productEntityMapper);
    }

    @Bean
    public ProductValidations productValidations() {
        return new ProductValidations(productPersistencePort());
    }

    @Bean
    public IProductRestHandler productRestHandler() {
        return new ProductRestHandlerImpl(productServicePort(), productRequestMapper, franchiseStockResponseMapper);
    }
}
