package com.example.franchise_challenge.infrastructure.adapters.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("franchise")
public class FranchiseEntity {
    @Id
    private Long id;
    private String name;
}

