package com.example.franchise_challenge.infrastructure.output.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductStockDTO {
    private String name;
    private String branchName;
    private Integer stock;
}
