package com.example.franchise_challenge.infrastructure.adapters.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("branch")
public class BranchEntity {
    @Id
    private Long id;
    private String name;
    private Long franchiseId;
}
