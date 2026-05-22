package com.josua.personal_finance_tracker.web;

import com.josua.personal_finance_tracker.dto.WalletMutationResponseDTO;
import com.josua.personal_finance_tracker.dto.WalletTopUpRequestDTO;
import com.josua.personal_finance_tracker.dto.WalletWithdrawRequestDTO;
import com.josua.personal_finance_tracker.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/topup")
    public ResponseEntity<String> topUp(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody WalletTopUpRequestDTO request) {
        return ResponseEntity.ok(walletService.topUp(userDetails.getUsername(), request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody WalletWithdrawRequestDTO request) {
        return ResponseEntity.ok(walletService.withdraw(userDetails.getUsername(), request));
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, Long>> getBalance(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long balance = walletService.getBalance(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("balance", balance));
    }

    @GetMapping("/mutations")
    public ResponseEntity<List<WalletMutationResponseDTO>> getMutations(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(walletService.getMutations(userDetails.getUsername()));
    }
}
