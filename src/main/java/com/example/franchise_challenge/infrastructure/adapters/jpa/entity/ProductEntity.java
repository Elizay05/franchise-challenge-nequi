package com.example.franchise_challenge.infrastructure.adapters.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("product")
public class ProductEntity {
    @Id
    private Long id;

    private String name;
}
