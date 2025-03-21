package com.example.franchise_challenge.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FranchiseStockResponse {
    private Long franchiseId;
    private List<ProductStockResponse> products;
}
