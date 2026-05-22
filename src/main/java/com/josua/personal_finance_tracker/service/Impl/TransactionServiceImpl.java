package com.josua.personal_finance_tracker.service.Impl;

import com.josua.personal_finance_tracker.domain.Transaction;
import com.josua.personal_finance_tracker.domain.User;
import com.josua.personal_finance_tracker.domain.Wallet;
import com.josua.personal_finance_tracker.dto.TransactionRequestDTO;
import com.josua.personal_finance_tracker.dto.TransactionResponseDTO;
import com.josua.personal_finance_tracker.repository.TransactionRepository;
import com.josua.personal_finance_tracker.repository.UserRepository;
import com.josua.personal_finance_tracker.repository.WalletRepository;
import com.josua.personal_finance_tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionResponseDTO createTransaction(String username, TransactionRequestDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        Transaction.TransactionType transactionType;
        try {
            transactionType = Transaction.TransactionType.valueOf(dto.transactionType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Transaction type must be EXPENSE or INCOME");
        }

        if (transactionType == Transaction.TransactionType.EXPENSE) {
            if (wallet.getBalance() < dto.amount()) {
                throw new RuntimeException("Saldo tidak cukup");
            }
            wallet.setBalance(wallet.getBalance() - dto.amount());
        } else {
            wallet.setBalance(wallet.getBalance() + dto.amount());
        }
        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setWallet(wallet);
        transaction.setCategory(dto.category());
        transaction.setAmount(dto.amount());
        transaction.setTransactionType(transactionType);
        transaction.setDescription(dto.description());
        transaction.setNote(dto.note());
        transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getTransactionType().name(),
                transaction.getDescription(),
                transaction.getNote(),
                transaction.getCategory(),
                transaction.getCreatedAt()
        );
    }

    @Override
    public List<TransactionResponseDTO> getTransactions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getAmount(),
                        t.getTransactionType().name(),
                        t.getDescription(),
                        t.getNote(),
                        t.getCategory(),
                        t.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByType(String username, String type) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction.TransactionType transactionType;
        try {
            transactionType = Transaction.TransactionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Transaction type must be EXPENSE or INCOME");
        }

        return transactionRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .filter(t -> t.getTransactionType() == transactionType)
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getAmount(),
                        t.getTransactionType().name(),
                        t.getDescription(),
                        t.getNote(),
                        t.getCategory(),
                        t.getCreatedAt()
                ))
                .toList();
    }
}
