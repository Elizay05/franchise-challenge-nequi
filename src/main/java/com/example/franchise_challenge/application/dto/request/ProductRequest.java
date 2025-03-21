package com.example.franchise_challenge.application.dto.request;

import com.example.franchise_challenge.application.utils.constants.ApplicationConstants;
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
    @NotBlank(message = ApplicationConstants.IS_REQUIRED)
    private String name;

    @NotNull(message = ApplicationConstants.IS_REQUIRED)
    private Long branchId;

    @Min(value = ApplicationConstants.MIN_STOCK, message = ApplicationConstants.MIN_STOCK_MESSAGE)
    private Integer stock;
}