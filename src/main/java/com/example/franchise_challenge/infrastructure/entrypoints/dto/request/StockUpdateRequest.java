package com.example.franchise_challenge.infrastructure.entrypoints.dto.request;

import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockUpdateRequest {

    @Min(value = InputConstants.MIN_STOCK, message = InputConstants.MIN_STOCK_MESSAGE)
    private int stock;
}
