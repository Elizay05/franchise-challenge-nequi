package com.example.franchise_challenge.infrastructure.entrypoints.dto.request;

import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequest {
    @NotBlank(message = InputConstants.IS_REQUIRED)
    private String name;

    @NotNull(message = InputConstants.IS_REQUIRED)
    private Long branchId;

    @Min(value = InputConstants.MIN_STOCK, message = InputConstants.MIN_STOCK_MESSAGE)
    private Integer stock;
}