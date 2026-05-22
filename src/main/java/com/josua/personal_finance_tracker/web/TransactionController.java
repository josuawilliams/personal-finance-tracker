package com.josua.personal_finance_tracker.web;

import com.josua.personal_finance_tracker.dto.TransactionRequestDTO;
import com.josua.personal_finance_tracker.dto.TransactionResponseDTO;
import com.josua.personal_finance_tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.createTransaction(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(transactionService.getTransactions(userDetails.getUsername()));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponseDTO>> getByType(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(userDetails.getUsername(), type));
    }
}
