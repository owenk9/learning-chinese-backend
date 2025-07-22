package com.example.backend.service;

public interface EmailService {
    void sendResetPasswordEmail(String to,String resetLink);
}
