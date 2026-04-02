package com.finance.finance_backend.repository;

import com.finance.finance_backend.entity.FinancialRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FinancialRecordsRepository extends JpaRepository<FinancialRecordEntity, Long>,
        JpaSpecificationExecutor<FinancialRecordEntity> {

}
