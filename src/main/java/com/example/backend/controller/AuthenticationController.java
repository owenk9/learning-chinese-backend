package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.LogoutRequest;
import com.example.backend.dto.request.SignUpRequest;
import com.example.backend.dto.request.TokenRefreshRequest;
import com.example.backend.dto.response.JwtResponse;
import com.example.backend.dto.response.TokenRefreshResponse;
import com.example.backend.entity.RefreshToken;
import com.example.backend.entity.User;
import com.example.backend.repository.RefreshTokenRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.UserPrincipal;
import com.example.backend.security.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
           return ResponseEntity.badRequest().body("Username is already in use");
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }
        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtil.generateAccessToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String refreshToken = jwtUtil.generateRefreshToken(userPrincipal.getUser().getId());

        RefreshToken refreshTokenEntity = new RefreshToken(
                refreshToken,
                LocalDateTime.now().plusDays(7),
                userRepository.findById(userPrincipal.getUser().getId()).get()
        );
        refreshTokenRepository.save(refreshTokenEntity);
        return ResponseEntity.ok().body(new JwtResponse(accessToken, refreshToken, userPrincipal.getUser().getId(),
                userPrincipal.getUser().getUsername(), userPrincipal.getUser().getEmail()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(requestRefreshToken);
        if(refreshTokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token not found");
        }
        RefreshToken refreshToken = refreshTokenOpt.get();
        if(refreshToken.isExpired()){
            refreshTokenRepository.delete(refreshToken);
            return ResponseEntity.badRequest().body("Refresh token expired");
        }
        if (!refreshToken.isValid()) {
                 refreshTokenRepository.delete(refreshToken);
                 return ResponseEntity.badRequest()
                     .body("Refresh token is invalid");
        }

        User user = refreshToken.getUser();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        String newAccessToken = jwtUtil.generateAccessToken(auth);

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, requestRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody LogoutRequest logoutRequest) {
        refreshTokenRepository.deleteByToken(logoutRequest.getRefreshToken());
        return ResponseEntity.ok("Log out successful!");
    }
}
