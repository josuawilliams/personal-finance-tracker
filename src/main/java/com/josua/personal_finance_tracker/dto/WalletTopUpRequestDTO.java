package com.josua.personal_finance_tracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WalletTopUpRequestDTO(
    @NotNull @Min(1) Long amount,
    String description
) {
}
