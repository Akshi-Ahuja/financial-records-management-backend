package com.finance.finance_backend.dto.responses;

import com.finance.finance_backend.enums.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CategoryTotalResponse {
    private Category category;
    private BigDecimal totalAmount;
}
