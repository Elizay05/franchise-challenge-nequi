package com.example.franchise_challenge.infrastructure.adapters.mysql.adapter;

import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.model.ProductStockModel;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.infrastructure.adapters.mysql.entity.ProductEntity;
import com.example.franchise_challenge.infrastructure.adapters.mysql.mapper.IProductEntityMapper;
import com.example.franchise_challenge.infrastructure.adapters.mysql.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ProductAdapter implements IProductPersistencePort {

    private final IProductRepository productRepository;
    private final IProductEntityMapper productEntityMapper;

    @Override
    public Mono<ProductModel> saveProduct(ProductModel productModel) {
        ProductEntity productEntity = productEntityMapper.toEntity(productModel);
        return productRepository.save(productEntity)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsProductByName(String productName) {
        return productRepository.existsProductEntitiesByName(productName);
    }

    @Override
    public Mono<Void> deleteProduct(Long productId) {
        return productRepository.deleteById(productId);
    }

    @Override
    public Mono<FranchiseStockModel> getProductStockByFranchiseId(Long franchiseId) {
        FranchiseStockModel productsWithFranchiseId = FranchiseStockModel.builder().build();

        return productRepository.findTopStockedProductsByFranchiseId(franchiseId)
                .collectList()
                .flatMap(
                        productList -> {
                            List<ProductStockModel> productStockList = productList.stream().map(
                                    dto -> new ProductStockModel(dto.getName(), dto.getBranchName(), dto.getStock())
                            ).toList();
                            productsWithFranchiseId.setFranchiseId(franchiseId);
                            productsWithFranchiseId.setProducts(productStockList);

                            return Mono.just(productsWithFranchiseId);
                        });
    }

    @Override
    public Mono<Void> updateName(Long productId, String name) {
        return productRepository.updateName(productId, name);
    }

    @Override
    public Mono<Boolean> existsProductById(Long productId) {
        return productRepository.existsById(productId);
    }
}
