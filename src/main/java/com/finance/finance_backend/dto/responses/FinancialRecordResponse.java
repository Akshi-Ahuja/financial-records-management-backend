package com.finance.finance_backend.dto.responses;

import com.finance.finance_backend.enums.Category;
import com.finance.finance_backend.enums.RecordType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class FinancialRecordResponse {
    private Long id;
    private BigDecimal amount;
    private RecordType type;
    private Category category;
    private LocalDate transactionDate;
    private String description;

    private Long createdByUserId;
    private String createdByName;
    private String createdByEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
