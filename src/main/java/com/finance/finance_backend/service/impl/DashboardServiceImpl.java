package com.finance.finance_backend.service.impl;

import com.finance.finance_backend.dto.responses.CategoryTotalResponse;
import com.finance.finance_backend.dto.responses.DashboardSummaryResponse;
import com.finance.finance_backend.dto.responses.FinancialRecordResponse;
import com.finance.finance_backend.dto.responses.MonthlyTrendResponse;
import com.finance.finance_backend.entity.FinancialRecordEntity;
import com.finance.finance_backend.enums.Category;
import com.finance.finance_backend.repository.FinancialRecordsRepository;
import com.finance.finance_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final FinancialRecordsRepository recordsRepository;

    @Override
    public DashboardSummaryResponse getDashboardSummary() {
        BigDecimal totalIncome = recordsRepository.getTotalIncome();
        BigDecimal totalExpense = recordsRepository.getTotalExpense();

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(totalIncome.subtract(totalExpense))
                .build();
    }

    @Override
    public List<CategoryTotalResponse> getCategoryTotals() {
        List<Object[]> results = recordsRepository.getCategoryTotals();

        return results.stream()
                .map(obj -> CategoryTotalResponse.builder()
                        .category((Category) obj[0])
                        .totalAmount((BigDecimal) obj[1])
                        .build())
                .toList();
    }

    @Override
    public List<FinancialRecordResponse> getRecentActivity() {
        Pageable pageable = PageRequest.of(0, 10);

        return recordsRepository.getRecentRecords(pageable)
                .stream().map(this::toRecordResponse)
                .toList();
    }

    @Override
    public List<MonthlyTrendResponse> getMonthlyTrends() {
        List<Object[]> results = recordsRepository.getMonthlyTrends();

        return results.stream()
                .map(obj -> {
                    BigDecimal totalIncome = (BigDecimal) obj[2];
                    BigDecimal totalExpense = (BigDecimal) obj[3];

                    return MonthlyTrendResponse.builder()
                            .year((Integer) obj[0])
                            .month((Integer) obj[1])
                            .totalIncome(totalIncome)
                            .totalExpense(totalExpense)
                            .netBalance(totalIncome.subtract(totalExpense))
                            .build();
                })
                .toList();
    }

    private FinancialRecordResponse toRecordResponse(FinancialRecordEntity record) {
        return FinancialRecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .transactionDate(record.getTransactionDate())
                .description(record.getDescription())
                .createdByUserId(record.getCreatedBy().getId())
                .createdByName(record.getCreatedBy().getName())
                .createdByEmail(record.getCreatedBy().getEmail())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
