package com.example.franchise_challenge.application.dto.request;

import com.example.franchise_challenge.application.utils.constants.ApplicationConstants;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockUpdateRequest {

    @Min(value = ApplicationConstants.MIN_STOCK, message = ApplicationConstants.MIN_STOCK_MESSAGE)
    private int stock;
}
