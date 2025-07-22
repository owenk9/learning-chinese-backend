package com.example.backend.service.Impl;

import com.example.backend.dto.request.ForgotPasswordRequest;
import com.example.backend.dto.request.ResetPasswordRequest;
import com.example.backend.dto.response.MessageResponse;
import com.example.backend.entity.PasswordResetToken;
import com.example.backend.entity.User;
import com.example.backend.repository.PasswordResetTokenRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public ResponseEntity<?> handleForgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email not found"));
        }

        User user = userOpt.get();

        Optional<PasswordResetToken> existingTokenOpt = passwordResetTokenRepository.findByUser(user);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);

        PasswordResetToken resetToken = existingTokenOpt.orElse(new PasswordResetToken());
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(expiry);

        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), resetLink);

        return ResponseEntity.ok(new MessageResponse("Password reset link sent to your email"));
    }


    @Override
    @Transactional
    public ResponseEntity<?> handleResetPassword(ResetPasswordRequest request) {
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(request.getToken());
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid reset token"));
        }

        PasswordResetToken token = tokenOpt.get();
        if (token.isExpired()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Reset token expired"));
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(token);

        return ResponseEntity.ok(new MessageResponse("Password reset successful"));
    }
}
