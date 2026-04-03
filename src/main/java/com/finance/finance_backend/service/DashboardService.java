package com.finance.finance_backend.service;

import com.finance.finance_backend.dto.responses.CategoryTotalResponse;
import com.finance.finance_backend.dto.responses.DashboardSummaryResponse;
import com.finance.finance_backend.dto.responses.FinancialRecordResponse;
import com.finance.finance_backend.dto.responses.MonthlyTrendResponse;

import java.util.List;

public interface DashboardService {
    DashboardSummaryResponse getDashboardSummary();

    List<CategoryTotalResponse> getCategoryTotals();

    List<FinancialRecordResponse> getRecentActivity();

    List<MonthlyTrendResponse> getMonthlyTrends();
}
