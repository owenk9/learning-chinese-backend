package com.example.backend.service;

import com.example.backend.dto.request.ForgotPasswordRequest;
import com.example.backend.dto.request.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> handleForgotPassword(ForgotPasswordRequest request);
    ResponseEntity<?> handleResetPassword(ResetPasswordRequest request);
    ResponseEntity<?> handleGoogleLogin(String googleToken);
}
