package com.josua.personal_finance_tracker.service;

import com.josua.personal_finance_tracker.dto.WalletMutationResponseDTO;
import com.josua.personal_finance_tracker.dto.WalletTopUpRequestDTO;
import com.josua.personal_finance_tracker.dto.WalletWithdrawRequestDTO;

import java.util.List;

public interface WalletService {
    String topUp(String username, WalletTopUpRequestDTO dto);
    String withdraw(String username, WalletWithdrawRequestDTO dto);
    Long getBalance(String username);
    List<WalletMutationResponseDTO> getMutations(String username);
}
