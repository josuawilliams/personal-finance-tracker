package com.josua.personal_finance_tracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionRequestDTO(
    @NotBlank String category,
    @NotNull @Min(1) Long amount,
    @NotBlank String transactionType,
    String description,
    String note
) {
}
