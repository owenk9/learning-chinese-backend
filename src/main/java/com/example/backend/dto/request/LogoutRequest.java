package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
    @NotBlank
    private String refreshToken;
    public LogoutRequest() {

    }
    public LogoutRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
