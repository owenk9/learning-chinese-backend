package com.example.backend.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
