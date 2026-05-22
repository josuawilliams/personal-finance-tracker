package com.josua.personal_finance_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank String username,
        @NotBlank String name,
        @NotBlank String password,
        @NotBlank @Email String email,
        ProfileRequestDTO profile) {

}
