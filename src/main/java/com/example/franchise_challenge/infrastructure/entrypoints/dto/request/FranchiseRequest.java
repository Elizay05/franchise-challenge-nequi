package com.example.franchise_challenge.infrastructure.entrypoints.dto.request;

import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FranchiseRequest {
    @NotBlank(message = InputConstants.IS_REQUIRED)
    private String name;
}
