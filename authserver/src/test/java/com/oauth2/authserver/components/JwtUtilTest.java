package com.oauth2.authserver.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Collections;
import java.util.Date;

public class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);
        System.out.println("[TOKEN] " + token);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);
        System.out.println("[TOKEN] " + token);
        System.out.println("[USERNAME] " + extractedUsername);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        JwtUtil jwtUtilWithShortExpiration = new JwtUtil() {
            @Override
            public String generateToken(String username) {
                return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
            }
        };

        String username = "testuser";
        String token = jwtUtilWithShortExpiration.generateToken(username);

        Thread.sleep(1500); // Espera 1.5 segundos para expirar

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractUsername(token));
    }
}
