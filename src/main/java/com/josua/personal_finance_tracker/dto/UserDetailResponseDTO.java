package com.josua.personal_finance_tracker.dto;

public record UserDetailResponseDTO(
    Long id,
    String username,
    String email,
    ProfileResponseDTO profile,
    Long walletBalance
) {
    public record ProfileResponseDTO(
        String gender,
        String address,
        String mobileNumber
    ) {
    }
}
