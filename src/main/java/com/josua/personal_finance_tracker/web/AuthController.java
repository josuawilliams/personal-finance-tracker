package com.josua.personal_finance_tracker.web;

import com.josua.personal_finance_tracker.dto.AuthResponseDTO;
import com.josua.personal_finance_tracker.dto.ChangePasswordRequestDTO;
import com.josua.personal_finance_tracker.dto.LoginRequestDTO;
import com.josua.personal_finance_tracker.dto.UserDetailResponseDTO;
import com.josua.personal_finance_tracker.dto.UserRequestDTO;
import com.josua.personal_finance_tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequestDTO request) {
        return ResponseEntity.ok(authService.changePassword(userDetails.getUsername(), request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailResponseDTO> getUserDetail(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(authService.getUserDetail(userDetails.getUsername()));
    }
}
