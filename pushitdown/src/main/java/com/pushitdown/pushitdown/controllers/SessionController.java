package com.pushitdown.pushitdown.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/user/token")
    public ResponseEntity<Map<String, String>> getToken(@AuthenticationPrincipal OAuth2User principal, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        Map<String, String> response = new HashMap<>();
        
        // Obtém o nome do usuário autenticado
        response.put("username", principal.getAttribute("name"));

        // Obtém o token de acesso
        response.put("access_token", authorizedClient.getAccessToken().getTokenValue());

        // Obtém o refresh token (se disponível)
        if (authorizedClient.getRefreshToken() != null) {
            response.put("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/info")
    public Map<String, Object> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            return oauth2User.getAttributes(); // Returns user info from OAuth2 provider
        }

        return Map.of("error", "User not authenticated");
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        Cookie cookie = new Cookie("SESSION", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Use only with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire immediately
        response.addCookie(cookie);
    }
}

