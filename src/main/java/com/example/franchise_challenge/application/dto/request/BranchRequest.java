package com.example.franchise_challenge.application.dto.request;

import com.example.franchise_challenge.application.utils.constants.ApplicationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BranchRequest {
    @NotBlank(message = ApplicationConstants.IS_REQUIRED)
    private String name;

    @NotNull(message = ApplicationConstants.IS_REQUIRED)
    private Integer franchiseId;
}

