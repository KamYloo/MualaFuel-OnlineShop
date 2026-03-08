package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.service.impl.JwtServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    JwtServiceImpl jwtService;
    String secret = Base64.getEncoder().encodeToString("testSecretKeyForJwtServiceTest1234567890".getBytes());

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "secretKey", secret);
    }

    @Test
    void testGenerateTokenAndValidate() {
        String username = "test@domain.com";
        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertTrue(jwtService.validateJwtToken(token));
        assertEquals(username, jwtService.extractUserName(token));
    }

    @Test
    void testValidateJwtTokenReturnsFalseForInvalidToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtService.validateJwtToken(invalidToken));
    }

    @Test
    void testExtractUserNameFromExpiredTokenThrows() {
        String username = "test@domain.com";
        long now = System.currentTimeMillis();
        long expired = now - 10000;
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now - 20000))
                .expiration(new Date(expired))
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)))
                .compact();

        assertThrows(ExpiredJwtException.class, () -> jwtService.extractUserName(expiredToken));
    }
}
