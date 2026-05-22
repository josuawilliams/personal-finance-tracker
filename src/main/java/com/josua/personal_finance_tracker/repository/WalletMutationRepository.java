package com.josua.personal_finance_tracker.repository;

import com.josua.personal_finance_tracker.domain.WalletMutation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletMutationRepository extends JpaRepository<WalletMutation, Long> {
    List<WalletMutation> findByWalletIdOrderByCreatedAtDesc(Long walletId);
}
