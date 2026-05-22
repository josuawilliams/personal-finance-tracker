package com.josua.personal_finance_tracker.service.Impl;

import com.josua.personal_finance_tracker.domain.User;
import com.josua.personal_finance_tracker.domain.Wallet;
import com.josua.personal_finance_tracker.domain.WalletMutation;
import com.josua.personal_finance_tracker.dto.WalletMutationResponseDTO;
import com.josua.personal_finance_tracker.dto.WalletTopUpRequestDTO;
import com.josua.personal_finance_tracker.dto.WalletWithdrawRequestDTO;
import com.josua.personal_finance_tracker.repository.UserRepository;
import com.josua.personal_finance_tracker.repository.WalletMutationRepository;
import com.josua.personal_finance_tracker.repository.WalletRepository;
import com.josua.personal_finance_tracker.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletMutationRepository mutationRepository;

    @Override
    @Transactional
    public String topUp(String username, WalletTopUpRequestDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance() + dto.amount());
        walletRepository.save(wallet);

        WalletMutation mutation = new WalletMutation();
        mutation.setWallet(wallet);
        mutation.setType(WalletMutation.MutationType.TOPUP);
        mutation.setAmount(dto.amount());
        mutation.setDescription(dto.description());
        mutationRepository.save(mutation);

        return "Top up berhasil";
    }

    @Override
    @Transactional
    public String withdraw(String username, WalletWithdrawRequestDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < dto.amount()) {
            throw new RuntimeException("Saldo tidak cukup");
        }

        wallet.setBalance(wallet.getBalance() - dto.amount());
        walletRepository.save(wallet);

        WalletMutation mutation = new WalletMutation();
        mutation.setWallet(wallet);
        mutation.setType(WalletMutation.MutationType.WITHDRAW);
        mutation.setAmount(dto.amount());
        mutation.setDescription(dto.description());
        mutationRepository.save(mutation);

        return "Withdraw berhasil";
    }

    @Override
    public Long getBalance(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return wallet.getBalance();
    }

    @Override
    public List<WalletMutationResponseDTO> getMutations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return mutationRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId())
                .stream()
                .map(m -> new WalletMutationResponseDTO(
                        m.getId(),
                        m.getType().name(),
                        m.getAmount(),
                        m.getDescription(),
                        m.getCreatedAt()
                ))
                .toList();
    }
}
