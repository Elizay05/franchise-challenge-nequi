package com.example.franchise_challenge.domain.model;

public class ProductBranchModel {
    private Long id;
    private Long productId;
    private Long branchId;
    private Integer stock;

    public ProductBranchModel(Long id, Long productId, Long branchId, Integer stock) {
        this.id = id;
        this.productId = productId;
        this.branchId = branchId;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
