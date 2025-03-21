package com.example.franchise_challenge.domain.model;

public class ProductModel {
    private Long id;
    private String name;
    private Long branchId;
    private Integer stock;

    public ProductModel(Long id, String name, Long branchId, Integer stock) {
        this.id = id;
        this.name = name;
        this.branchId = branchId;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getBranchId() {
        return branchId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
