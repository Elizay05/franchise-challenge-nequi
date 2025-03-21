package com.example.franchise_challenge.domain.exceptions;

public class ProductNotFoundInBranchException extends RuntimeException {
    public ProductNotFoundInBranchException(String message) {
        super(message);
    }
}
