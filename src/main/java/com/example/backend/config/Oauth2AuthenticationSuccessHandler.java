//package com.example.backend.config;
//
//import com.example.backend.security.jwt.JwtUtil;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        String accessToken = jwtUtil.generateAccessToken(authentication);
//        String redirectUrl = "http://localhost:3000/oauth2/success?token=" + accessToken;
//        response.sendRedirect(redirectUrl);
//    }
//}
