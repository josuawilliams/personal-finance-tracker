package com.josua.personal_finance_tracker.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank String username,
    @NotBlank String password
) {
}
