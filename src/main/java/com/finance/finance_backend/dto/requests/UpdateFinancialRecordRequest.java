package com.finance.finance_backend.dto.requests;

import com.finance.finance_backend.enums.Category;
import com.finance.finance_backend.enums.RecordType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateFinancialRecordRequest {
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private RecordType type;

    private Category category;

    private LocalDate transactionDate;

    @Size(max = 500, message = "Description can be maximum 500 characters")
    private String description;
}
