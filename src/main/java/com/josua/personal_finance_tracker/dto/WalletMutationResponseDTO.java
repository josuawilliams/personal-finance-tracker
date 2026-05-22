package com.josua.personal_finance_tracker.dto;

import java.time.LocalDateTime;

public record WalletMutationResponseDTO(
    Long id,
    String type,
    Long amount,
    String description,
    LocalDateTime createdAt
) {
}
