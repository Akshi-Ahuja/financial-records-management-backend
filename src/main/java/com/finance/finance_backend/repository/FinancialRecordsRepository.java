package com.finance.finance_backend.repository;

import com.finance.finance_backend.entity.FinancialRecordEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialRecordsRepository extends JpaRepository<FinancialRecordEntity, Long>,
        JpaSpecificationExecutor<FinancialRecordEntity> {

    @Query("SELECT COALESCE(SUM(f.amount),0) FROM FinancialRecordEntity f WHERE f.type = 'INCOME'")
    BigDecimal getTotalIncome();

    @Query("SELECT COALESCE(SUM(f.amount),0) FROM FinancialRecordEntity f WHERE f.type = 'EXPENSE'")
    BigDecimal getTotalExpense();

    @Query("""
            SELECT f.category, SUM(f.amount)
            FROM FinancialRecordEntity f
            GROUP BY f.category
            """)
    List<Object[]> getCategoryTotals();

    @Query("""
            SELECT f from FinancialRecordEntity f
            ORDER BY f.createdAt Desc  
            """)
    List<FinancialRecordEntity> getRecentRecords(Pageable pageable);

    @Query("""
            SELECT YEAR(f.transactionDate), MONTH(f.transactionDate),
                   SUM(CASE WHEN f.type = 'INCOME' THEN f.amount ELSE 0 END),
                   SUM(CASE WHEN f.type = 'EXPENSE' THEN f.amount ELSE 0 END)
            FROM FinancialRecordEntity f
            GROUP BY YEAR(f.transactionDate), MONTH(f.transactionDate)
            ORDER BY YEAR(f.transactionDate), MONTH(f.transactionDate)
            """)
    List<Object[]> getMonthlyTrends();

}
