package com.example.franchise_challenge.domain.model;

public class ProductStockModel {
    private String name;
    private String branchName;
    private Integer stock;

    public ProductStockModel(String name, String branchName, Integer stock) {
        this.name = name;
        this.branchName = branchName;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
