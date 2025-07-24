package com.example.backend.dto.request;

import com.example.backend.validator.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String token;
    @NotBlank
    @ValidPassword
    private String newPassword;
}
