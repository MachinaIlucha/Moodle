package com.illiapinchuk.moodle.configuration.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

  @Mock private JwtTokenProvider jwtTokenProvider;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  @Mock private Authentication authentication;

  @InjectMocks private JwtTokenFilter jwtTokenFilter;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void doFilter_whenTokenIsNull_thenDoFilterAndDoNotAuthenticate()
      throws IOException, ServletException {
    when(jwtTokenProvider.resolveToken(any())).thenReturn(null);

    jwtTokenFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verify(jwtTokenProvider, never()).getAuthentication(any());
    verify(jwtTokenProvider, never()).validateToken(any());
  }

  @Test
  void doFilter_whenTokenIsInvalid_thenDoFilterAndDoNotAuthenticate()
      throws IOException, ServletException {
    when(jwtTokenProvider.resolveToken(any())).thenReturn("invalid");
    when(jwtTokenProvider.validateToken(any())).thenReturn(false);

    jwtTokenFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verify(jwtTokenProvider, never()).getAuthentication(any());
  }

  @Test
  void doFilter_whenTokenIsValid_thenDoFilterAndAuthenticate()
      throws IOException, ServletException {
    when(jwtTokenProvider.resolveToken(any())).thenReturn("valid");
    when(jwtTokenProvider.validateToken(any())).thenReturn(true);
    when(jwtTokenProvider.getAuthentication(any())).thenReturn(authentication);

    jwtTokenFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verify(jwtTokenProvider).getAuthentication(any());
  }
}
