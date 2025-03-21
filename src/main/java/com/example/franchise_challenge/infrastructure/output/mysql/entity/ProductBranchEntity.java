package com.example.franchise_challenge.infrastructure.output.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("product_branch")
@Builder
public class ProductBranchEntity {
    @Id
    private Long id;
    private Long productId;
    private Long branchId;
    private Integer stock;
}
