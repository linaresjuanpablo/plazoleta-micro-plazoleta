package com.gastropolis.plazoleta.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET_KEY = "c3VwZXJTZWNyZXRLZXlGb3JUZXN0aW5nUHVycG9zZXNPbmx5MTIz";

    private JwtService jwtService;
    private String validToken;
    private String tokenWithLongUserId;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        validToken = Jwts.builder()
                .setSubject("test@email.com")
                .claim("userId", 5)
                .claim("role", "CLIENTE")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        tokenWithLongUserId = Jwts.builder()
                .setSubject("admin@email.com")
                .claim("userId", 3000000000L)
                .claim("role", "ADMINISTRADOR")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        expiredToken = Jwts.builder()
                .setSubject("expired@email.com")
                .claim("userId", 1)
                .claim("role", "CLIENTE")
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(key)
                .compact();
    }

    @Test
    void extractUsername_returnsSubject() {
        String username = jwtService.extractUsername(validToken);
        assertEquals("test@email.com", username);
    }

    @Test
    void extractUserId_whenStoredAsInteger_returnsLong() {
        Long userId = jwtService.extractUserId(validToken);
        assertEquals(5L, userId);
    }

    @Test
    void extractUserId_whenStoredAsLong_returnsLong() {
        Long userId = jwtService.extractUserId(tokenWithLongUserId);
        assertEquals(3000000000L, userId);
    }

    @Test
    void extractRole_returnsRole() {
        String role = jwtService.extractRole(validToken);
        assertEquals("CLIENTE", role);
    }

    @Test
    void extractRole_withAdminRole_returnsAdministrador() {
        String role = jwtService.extractRole(tokenWithLongUserId);
        assertEquals("ADMINISTRADOR", role);
    }

    @Test
    void isTokenValid_withValidToken_returnsTrue() {
        assertTrue(jwtService.isTokenValid(validToken));
    }

    @Test
    void isTokenValid_withExpiredToken_returnsFalse() {
        assertFalse(jwtService.isTokenValid(expiredToken));
    }

    @Test
    void isTokenValid_withInvalidToken_returnsFalse() {
        assertFalse(jwtService.isTokenValid("invalid.token.string"));
    }

    @Test
    void getUserIdFromToken_returnsUserId() {
        Long userId = jwtService.getUserIdFromToken(validToken);
        assertEquals(5L, userId);
    }

    @Test
    void extractClaim_returnsCorrectClaim() {
        String subject = jwtService.extractClaim(validToken, claims -> claims.getSubject());
        assertEquals("test@email.com", subject);
    }

    @Test
    void extractUserId_whenNoUserIdClaim_returnsNull() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String tokenWithoutUserId = Jwts.builder()
                .setSubject("nouserid@email.com")
                .claim("role", "CLIENTE")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        Long userId = jwtService.extractUserId(tokenWithoutUserId);
        assertNull(userId);
    }
}
