package com.pushitdown.pushitdown.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Controller
public class RedirectController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public RedirectController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/redirect")
    public String redirect(@AuthenticationPrincipal OAuth2User oauth2User,
                           HttpServletResponse response,
                           Model model) {

        System.out.println("[REDIRECT]: redirecting...");

        // Obtém o cliente OAuth2 autenticado
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("pushitdown", oauth2User.getName());

        if (authorizedClient == null) {
            return "redirect:/login";
        }

        // Obtém o token de acesso
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        String tokenValue = accessToken.getTokenValue();

        // Chama os endpoints protegidos
        RestTemplate restTemplate = new RestTemplate();

        // Obtém o JSON do token
        ResponseEntity<String> tokenResponse = restTemplate.getForEntity(
                "http://client.local:8080/user/token", String.class);

        // Obtém o JSON das informações do usuário
        ResponseEntity<String> userInfoResponse = restTemplate.getForEntity(
                "http://client.local:8080/user/info", String.class);

        // Salva os JSONs nos cookies (codificados em Base64 para evitar problemas)
        addCookie(response, "tokenData", tokenResponse.getBody());
        addCookie(response, "userInfo", userInfoResponse.getBody());

        // Redireciona para a página de redirecionamento
        return "redirect:/redirecting.html";
    }

    private void addCookie(HttpServletResponse response, String name, String value) {
        String encodedValue = Base64.getEncoder().encodeToString(value.getBytes());
        Cookie cookie = new Cookie(name, encodedValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 hora
        response.addCookie(cookie);
    }
}
