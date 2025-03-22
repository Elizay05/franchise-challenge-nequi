package com.example.franchise_challenge.application.config;

import com.example.franchise_challenge.domain.spi.IProductBranchPersistencePort;
import com.example.franchise_challenge.domain.validationsUseCase.ProductBranchValidations;
import com.example.franchise_challenge.infrastructure.adapters.mysql.adapter.ProductBranchAdapter;
import com.example.franchise_challenge.infrastructure.adapters.mysql.mapper.IProductBranchEntityMapper;
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
import com.example.franchise_challenge.infrastructure.adapters.mysql.adapter.BranchAdapter;
import com.example.franchise_challenge.infrastructure.adapters.mysql.adapter.FranchiseAdapter;
import com.example.franchise_challenge.infrastructure.adapters.mysql.adapter.ProductAdapter;
import com.example.franchise_challenge.infrastructure.adapters.mysql.mapper.IBranchEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.mysql.mapper.IFranchiseEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.mysql.mapper.IProductEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.mysql.repository.IBranchRepository;
import com.example.franchise_challenge.infrastructure.adapters.mysql.repository.IFranchiseRepository;
import com.example.franchise_challenge.infrastructure.adapters.mysql.repository.IProductBranchRepository;
import com.example.franchise_challenge.infrastructure.adapters.mysql.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final IFranchiseRepository franchiseRepository;
    private final IFranchiseEntityMapper franchiseEntityMapper;
    private final IBranchRepository branchRepository;
    private final IBranchEntityMapper branchEntityMapper;
    private final IProductRepository productRepository;
    private final IProductBranchRepository productBranchRepository;
    private final IProductBranchEntityMapper productBranchEntityMapper;
    private final IProductEntityMapper productEntityMapper;

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
    public IProductServicePort productServicePort() {
        return new ProductUseCase(productPersistencePort(), productBranchPersistencePort(), productValidations(), branchValidations(), franchiseValidations(), productBranchValidations());
    }

    @Bean
    public IProductPersistencePort productPersistencePort() {
        return new ProductAdapter(productRepository, productEntityMapper);
    }

    @Bean
    public IProductBranchPersistencePort productBranchPersistencePort() {
        return new ProductBranchAdapter(productBranchRepository, productBranchEntityMapper);
    }

    @Bean
    public ProductValidations productValidations() {
        return new ProductValidations(productPersistencePort());
    }

    @Bean
    public ProductBranchValidations productBranchValidations() {
        return new ProductBranchValidations(productBranchPersistencePort());
    }
}
