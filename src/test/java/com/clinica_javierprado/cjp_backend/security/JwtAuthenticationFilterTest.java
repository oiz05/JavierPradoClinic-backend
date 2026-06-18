package com.clinica_javierprado.cjp_backend.security;

import com.clinica_javierprado.cjp_backend.domain.Role;
import com.clinica_javierprado.cjp_backend.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    private final JwtService jwtService = mock(JwtService.class);
    private final UserDetailsService userDetailsService = mock(UserDetailsService.class);
    private final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doesNotAuthenticateUnverifiedUserFromExistingToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        User user = User.builder()
                .email("ana@test.com")
                .emailVerified(false)
                .role(Role.PATIENT)
                .password("password")
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer old-token");
        when(jwtService.extractUsername("old-token")).thenReturn("ana@test.com");
        when(userDetailsService.loadUserByUsername("ana@test.com")).thenReturn(user);

        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtService, never()).isTokenValid("old-token", user);
        verify(chain).doFilter(request, response);
    }
}
