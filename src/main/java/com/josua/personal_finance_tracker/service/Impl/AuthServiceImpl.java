package com.josua.personal_finance_tracker.service.Impl;

import com.josua.personal_finance_tracker.domain.Profile;
import com.josua.personal_finance_tracker.domain.User;
import com.josua.personal_finance_tracker.domain.Wallet;
import com.josua.personal_finance_tracker.dto.AuthResponseDTO;
import com.josua.personal_finance_tracker.dto.ChangePasswordRequestDTO;
import com.josua.personal_finance_tracker.dto.LoginRequestDTO;
import com.josua.personal_finance_tracker.dto.UserDetailResponseDTO;
import com.josua.personal_finance_tracker.dto.UserRequestDTO;
import com.josua.personal_finance_tracker.repository.UserRepository;
import com.josua.personal_finance_tracker.repository.WalletRepository;
import com.josua.personal_finance_tracker.service.AuthService;
import com.josua.personal_finance_tracker.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(UserRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        if (dto.profile() != null) {
            Profile profile = new Profile();
            profile.setGender(dto.profile().gender());
            profile.setAddress(dto.profile().address());
            profile.setMobileNumber(dto.profile().mobileNumber());
            profile.setUser(user);
            user.setProfile(profile);
        }

        userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0L);
        walletRepository.save(wallet);

        return "Berhasil Register";
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new RuntimeException("Username atau password salah");
        }

        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);
        return new AuthResponseDTO(token, user.getUsername(), user.getEmail());
    }

    @Override
    public String changePassword(String username, ChangePasswordRequestDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);

        return "Password berhasil diubah";
    }

    @Override
    public UserDetailResponseDTO getUserDetail(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElse(null);

        UserDetailResponseDTO.ProfileResponseDTO profileDTO = null;
        if (user.getProfile() != null) {
            profileDTO = new UserDetailResponseDTO.ProfileResponseDTO(
                    user.getProfile().getGender(),
                    user.getProfile().getAddress(),
                    user.getProfile().getMobileNumber()
            );
        }

        return new UserDetailResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profileDTO,
                wallet != null ? wallet.getBalance() : 0L
        );
    }
}
