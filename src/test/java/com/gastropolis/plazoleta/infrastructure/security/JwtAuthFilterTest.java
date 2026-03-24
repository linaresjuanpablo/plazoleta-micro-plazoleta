package com.gastropolis.plazoleta.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_whenNoAuthorizationHeader_passesThrough() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilterInternal_whenAuthHeaderWithoutBearer_passesThrough() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilterInternal_whenTokenIsInvalid_passesThrough() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        when(jwtService.isTokenValid("invalidtoken")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtService).isTokenValid("invalidtoken");
    }

    @Test
    void doFilterInternal_whenValidToken_setsAuthenticationInContext() throws Exception {
        String token = "validtoken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn("user@email.com");
        when(jwtService.extractRole(token)).thenReturn("CLIENTE");
        when(jwtService.getUserIdFromToken(token)).thenReturn(5L);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user@email.com", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertEquals(5L, SecurityContextHolder.getContext().getAuthentication().getDetails());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("CLIENTE")));
    }

    @Test
    void doFilterInternal_whenValidTokenButAuthAlreadySet_doesNotOverrideAuth() throws Exception {
        String token = "validtoken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn("user@email.com");
        when(jwtService.extractRole(token)).thenReturn("CLIENTE");
        when(jwtService.getUserIdFromToken(token)).thenReturn(5L);

        // First call sets the auth
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        // Second call should not override (authentication already present)
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // filterChain should be called twice
        verify(filterChain, times(2)).doFilter(request, response);
        // extractUsername should only be called once (first call)
        verify(jwtService, times(1)).extractUsername(token);
    }
}
