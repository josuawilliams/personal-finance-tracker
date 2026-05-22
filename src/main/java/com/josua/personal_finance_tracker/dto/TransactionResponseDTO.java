package com.josua.personal_finance_tracker.dto;

import java.time.LocalDateTime;

public record TransactionResponseDTO(
    Long id,
    Long amount,
    String transactionType,
    String description,
    String note,
    String category,
    LocalDateTime createdAt
) {
}
