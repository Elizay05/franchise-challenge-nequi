package com.example.franchise_challenge.infrastructure.entrypoints.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductStockResponse {
    private String name;
    private String branchName;
    private Integer stock;
}
