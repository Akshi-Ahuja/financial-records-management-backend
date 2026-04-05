package com.finance.finance_backend.service;

import com.finance.finance_backend.dto.requests.CreateFinancialRecordRequest;
import com.finance.finance_backend.dto.requests.UpdateFinancialRecordRequest;
import com.finance.finance_backend.dto.responses.FinancialRecordResponse;
import com.finance.finance_backend.dto.responses.PagedResponse;
import com.finance.finance_backend.enums.Category;
import com.finance.finance_backend.enums.RecordType;

import java.time.LocalDate;

public interface FinancialRecordService {

    FinancialRecordResponse createRecord(CreateFinancialRecordRequest request);

    PagedResponse<FinancialRecordResponse> getAllRecords(
            RecordType type,
            Category category,
            LocalDate startDate,
            LocalDate endDate,
            String search,
            int page,
            int size
    );

    FinancialRecordResponse getRecordById(Long recordId);

    FinancialRecordResponse updateRecord(Long recordId, UpdateFinancialRecordRequest request);

    void deleteRecord(Long recordId);

}
