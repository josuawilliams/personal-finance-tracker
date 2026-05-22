package com.josua.personal_finance_tracker.dto;

import jakarta.validation.constraints.Pattern;

public record ProfileRequestDTO(
        String gender,
        String address,
        @Pattern(regexp = "^08\\d{10}$") String mobileNumber) {

}
