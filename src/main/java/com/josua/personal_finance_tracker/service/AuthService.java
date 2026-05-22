package com.josua.personal_finance_tracker.service;

import com.josua.personal_finance_tracker.dto.AuthResponseDTO;
import com.josua.personal_finance_tracker.dto.ChangePasswordRequestDTO;
import com.josua.personal_finance_tracker.dto.LoginRequestDTO;
import com.josua.personal_finance_tracker.dto.UserDetailResponseDTO;
import com.josua.personal_finance_tracker.dto.UserRequestDTO;

public interface AuthService {
    String register(UserRequestDTO userRequestDTO);
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
    String changePassword(String username, ChangePasswordRequestDTO changePasswordRequestDTO);
    UserDetailResponseDTO getUserDetail(String username);
}
