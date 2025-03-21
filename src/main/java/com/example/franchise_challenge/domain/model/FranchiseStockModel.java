package com.example.franchise_challenge.domain.model;

import lombok.Builder;

import java.util.List;

@Builder
public class FranchiseStockModel {
    private Long franchiseId;
    private List<ProductStockModel> products;

    public FranchiseStockModel(Long franchiseId, List<ProductStockModel> products) {
        this.franchiseId = franchiseId;
        this.products = products;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public List<ProductStockModel> getProducts() {
        return products;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public void setProducts(List<ProductStockModel> products) {
        this.products = products;
    }
}
