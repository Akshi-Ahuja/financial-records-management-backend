package com.finance.finance_backend.controller;

import com.finance.finance_backend.dto.responses.CategoryTotalResponse;
import com.finance.finance_backend.dto.responses.DashboardSummaryResponse;
import com.finance.finance_backend.dto.responses.FinancialRecordResponse;
import com.finance.finance_backend.dto.responses.MonthlyTrendResponse;
import com.finance.finance_backend.service.impl.DashboardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardServiceImpl dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary(){
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

    @GetMapping("/category-totals")
    public ResponseEntity<List<CategoryTotalResponse>> getCategoryTotals(){
        return ResponseEntity.ok(dashboardService.getCategoryTotals());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<FinancialRecordResponse>> getRecentActivity(){
        return ResponseEntity.ok(dashboardService.getRecentActivity());
    }

    @GetMapping("/monthly-trend")
    public ResponseEntity<List<MonthlyTrendResponse>> getMonthlyTrend(){
        return ResponseEntity.ok(dashboardService.getMonthlyTrends());
    }
}
