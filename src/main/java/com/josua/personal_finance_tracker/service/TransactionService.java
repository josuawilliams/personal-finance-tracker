package com.josua.personal_finance_tracker.service;

import com.josua.personal_finance_tracker.dto.TransactionRequestDTO;
import com.josua.personal_finance_tracker.dto.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {
    TransactionResponseDTO createTransaction(String username, TransactionRequestDTO dto);
    List<TransactionResponseDTO> getTransactions(String username);
    List<TransactionResponseDTO> getTransactionsByType(String username, String type);
}
