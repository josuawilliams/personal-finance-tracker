package com.josua.personal_finance_tracker.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDTO(
    @NotBlank String oldPassword,
    @NotBlank String newPassword
) {
}
