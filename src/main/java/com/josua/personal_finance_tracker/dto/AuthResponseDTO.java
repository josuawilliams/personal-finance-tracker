package com.josua.personal_finance_tracker.dto;

public record AuthResponseDTO(
    String token,
    String username,
    String email
) {
}
