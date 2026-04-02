package com.finance.finance_backend.controller;

import com.finance.finance_backend.dto.requests.CreateFinancialRecordRequest;
import com.finance.finance_backend.dto.requests.UpdateFinancialRecordRequest;
import com.finance.finance_backend.dto.responses.FinancialRecordResponse;
import com.finance.finance_backend.enums.Category;
import com.finance.finance_backend.enums.RecordType;
import com.finance.finance_backend.service.FinancialRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    @PostMapping
    public ResponseEntity<FinancialRecordResponse> createRecord
            (@Valid @RequestBody CreateFinancialRecordRequest request){
        return new ResponseEntity<>(financialRecordService.createRecord(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FinancialRecordResponse>> getAllRecords(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        return ResponseEntity.ok(financialRecordService.getAllRecords(type, category, startDate, endDate));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<FinancialRecordResponse> getRecordById(@PathVariable Long recordId){
        return ResponseEntity.ok(financialRecordService.getRecordById(recordId));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<FinancialRecordResponse> updateRecord(@PathVariable Long recordId,
                                                                @Valid @RequestBody UpdateFinancialRecordRequest request){
        return ResponseEntity.ok(financialRecordService.updateRecord(recordId, request));
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long recordId){
        financialRecordService.deleteRecord(recordId);
        return ResponseEntity.ok("Record Deleted Successfully!");
    }

}
