package com.finance.finance_backend.service.impl;

import com.finance.finance_backend.dto.requests.CreateFinancialRecordRequest;
import com.finance.finance_backend.dto.requests.UpdateFinancialRecordRequest;
import com.finance.finance_backend.dto.responses.FinancialRecordResponse;
import com.finance.finance_backend.entity.FinancialRecordEntity;
import com.finance.finance_backend.entity.UserEntity;
import com.finance.finance_backend.enums.Category;
import com.finance.finance_backend.enums.RecordType;
import com.finance.finance_backend.repository.FinancialRecordsRepository;
import com.finance.finance_backend.repository.UserRepository;
import com.finance.finance_backend.service.FinancialRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialRecordServiceImpl implements FinancialRecordService {
    private final UserRepository userRepository;
    private final FinancialRecordsRepository recordsRepository;


    @Override
    public FinancialRecordResponse createRecord(CreateFinancialRecordRequest request) {
        if (request.getCreatedByUserId() == null) {
            throw new RuntimeException("CreatedBy user ID is required");
        }

        UserEntity found = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found!"));

        validateTypeCategory(request.getType(), request.getCategory());

        FinancialRecordEntity newRecord = FinancialRecordEntity.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .transactionDate(request.getTransactionDate())
                .description(request.getDescription())
                .createdBy(found)
                .build();

        FinancialRecordEntity saved = recordsRepository.save(newRecord);
        return toRecordResponse(saved);
    }

    @Override
    public List<FinancialRecordResponse> getAllRecords(
            RecordType type,
            Category category,
            LocalDate startDate,
            LocalDate endDate) {

        if (type != null && category != null) {
            validateTypeCategory(type, category);
        }

        Specification<FinancialRecordEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();

        if (type != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }

        if (category != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("category"), category));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("transactionDate"), startDate, endDate));
        }

        List<FinancialRecordEntity> records = recordsRepository.findAll(spec);

        return records.stream()
                .map(this::toRecordResponse)
                .toList();

    }

    @Override
    public FinancialRecordResponse getRecordById(Long recordId) {
        FinancialRecordEntity found = recordsRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Financial record not found!"));

        return toRecordResponse(found);
    }

    @Override
    public FinancialRecordResponse updateRecord(Long recordId, UpdateFinancialRecordRequest request) {

        FinancialRecordEntity found = recordsRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Financial record not found!"));

        RecordType finalType = request.getType() != null ? request.getType() : found.getType();
        Category finalCategory = request.getCategory() != null ? request.getCategory() : found.getCategory();

        validateTypeCategory(finalType, finalCategory);

        if (request.getAmount() != null) {
            found.setAmount(request.getAmount());
        }

        if (request.getType() != null) {
            found.setType(request.getType());
        }

        if (request.getCategory() != null) {
            found.setCategory(request.getCategory());
        }

        if (request.getTransactionDate() != null) {
            found.setTransactionDate(request.getTransactionDate());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            found.setDescription(request.getDescription());
        }

        FinancialRecordEntity updated = recordsRepository.save(found);

        return toRecordResponse(updated);
    }

    @Override
    public void deleteRecord(Long recordId) {
        FinancialRecordEntity found = recordsRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Financial record not found!"));

        recordsRepository.delete(found);
    }

    private void validateTypeCategory(RecordType type, Category category) {

        if (type == RecordType.INCOME) {
            if (category == Category.RENT ||
                    category == Category.SALARY ||
                    category == Category.SOFTWARE ||
                    category == Category.MARKETING ||
                    category == Category.TRAVEL ||
                    category == Category.OFFICE_SUPPLIES ||
                    category == Category.HARDWARE ||
                    category == Category.TAX ||
                    category == Category.FOOD ||
                    category == Category.MAINTENANCE ||
                    category == Category.TRAINING ||
                    category == Category.MISCELLANEOUS) {

                throw new RuntimeException("Invalid category for Income");
            }
        }

        if (type == RecordType.EXPENSE) {
            if (category == Category.CLIENT_PAYMENT ||
                    category == Category.SALES_REVENUE ||
                    category == Category.SUBSCRIPTION_REVENUE ||
                    category == Category.CONSULTING_INCOME ||
                    category == Category.INVESTMENT ||
                    category == Category.REFUND_INCOME ||
                    category == Category.INTEREST_INCOME) {

                throw new RuntimeException("Invalid category for Expense");
            }
        }
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
