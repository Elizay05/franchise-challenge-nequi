package com.example.franchise_challenge.domain.exceptions;

public class FranchiseAlreadyExistsException extends RuntimeException {
    public FranchiseAlreadyExistsException(String message) {
        super(message);
    }
}
