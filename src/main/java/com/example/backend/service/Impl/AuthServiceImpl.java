package com.example.backend.service.Impl;

import com.example.backend.dto.request.ForgotPasswordRequest;
import com.example.backend.dto.request.ResetPasswordRequest;
import com.example.backend.dto.response.MessageResponse;
import com.example.backend.entity.PasswordResetToken;
import com.example.backend.entity.RefreshToken;
import com.example.backend.entity.User;
import com.example.backend.repository.PasswordResetTokenRepository;
import com.example.backend.repository.RefreshTokenRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.GoogleOauth2;
import com.example.backend.security.UserPrincipal;
import com.example.backend.security.jwt.JwtUtil;
import com.example.backend.service.AuthService;
import com.example.backend.service.EmailService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
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
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private GoogleOauth2 googleOauth2;
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

    @Override
    public ResponseEntity<?> handleGoogleLogin(String googleToken) {
        GoogleIdToken.Payload payload = googleOauth2.verifyGoogleToken(googleToken);
        if (payload == null || !Boolean.TRUE.equals(payload.getEmailVerified())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
        }

        String email = payload.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(email.split("@")[0]);
                    return userRepository.save(newUser);
                });

        UserDetails userDetails = UserPrincipal.create(user);



        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getUser().getId();
        String accessToken = jwtUtil.generateAccessToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(userId);
        RefreshToken refreshTokenEntity = new RefreshToken(
                refreshToken,
                LocalDateTime.now().plusDays(7),
                user
        );
        refreshTokenRepository.save(refreshTokenEntity);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "email", user.getEmail(),
                "username", user.getUsername()
        ));
    }
}
