package com.finance.finance_backend.exception;

public class InvalidEmailDomainException extends RuntimeException{
    public InvalidEmailDomainException(String message) {
        super(message);
    }
}
