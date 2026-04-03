package com.finance.finance_backend.exception;

public class FinancialRecordNotFoundException extends RuntimeException{
    public FinancialRecordNotFoundException(String message) {
        super(message);
    }
}
