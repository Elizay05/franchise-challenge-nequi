package com.example.franchise_challenge.infrastructure.output.mysql.adapter;

import com.example.franchise_challenge.domain.model.ProductModel;
import com.example.franchise_challenge.domain.model.ProductStockModel;
import com.example.franchise_challenge.domain.model.FranchiseStockModel;
import com.example.franchise_challenge.domain.spi.IProductPersistencePort;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.ProductBranchEntity;
import com.example.franchise_challenge.infrastructure.output.mysql.entity.ProductEntity;
import com.example.franchise_challenge.infrastructure.output.mysql.mapper.IProductEntityMapper;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IProductBranchRepository;
import com.example.franchise_challenge.infrastructure.output.mysql.repository.IProductRepository;
import com.example.franchise_challenge.infrastructure.output.mysql.utils.constants.OutputConstants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ProductAdapter implements IProductPersistencePort {

    private final IProductRepository productRepository;
    private final IProductBranchRepository productBranchRepository;
    private final IProductEntityMapper productEntityMapper;

    @Override
    public Mono<Void> saveProduct(ProductModel productModel) {
        ProductEntity productEntity = productEntityMapper.toEntity(productModel);
        return productRepository.save(productEntity)
                .flatMap(savedProduct -> {
                    ProductBranchEntity productBranchEntity = ProductBranchEntity.builder()
                            .productId(savedProduct.getId())
                            .branchId(productModel.getBranchId())
                            .stock(productModel.getStock())
                            .build();

                    return productBranchRepository.save(productBranchEntity);
                })
                .then();
    }

    @Override
    public Mono<Boolean> existsProductByName(String productName) {
        return productRepository.existsProductEntitiesByName(productName);
    }

    @Override
    public Mono<Boolean> productExistsInBranch(Long productId, Long branchId) {
        return productBranchRepository.existsByProductIdAndBranchId(productId, branchId);
    }

    @Override
    public Mono<Void> deleteProductFromBranch(Long productId, Long branchId) {
        return productBranchRepository.deleteByProductIdAndBranchId(productId, branchId)
                .then(productBranchRepository.countByProductId(productId))
                .flatMap(count -> count == OutputConstants.ZERO ? productRepository.deleteById(productId) : Mono.empty());
    }

    @Override
    public Mono<Void> updateStock(Long productId, Long branchId, int stock) {
        return productBranchRepository.updateStock(productId, branchId, stock);
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
