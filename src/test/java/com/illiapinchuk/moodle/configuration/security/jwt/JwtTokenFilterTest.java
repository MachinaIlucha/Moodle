package com.illiapinchuk.moodle.configuration.security.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {
  private final String TOKEN = "token";
  @Mock private MockHttpServletRequest request;
  @Mock private MockHttpServletResponse response;
  @Mock private FilterChain chain;
  @Mock private JwtTokenProvider jwtTokenProvider;
  @InjectMocks private JwtTokenFilter jwtTokenFilter;

  @AfterEach
  public void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void doFilterShouldCallChainDoFilterOnesAndContextOfSecurityContextHolderShouldBeNull()
      throws ServletException, IOException {
    when(jwtTokenProvider.resolveToken(request)).thenReturn(TOKEN);
    when(jwtTokenProvider.validateToken(TOKEN)).thenReturn(true);
    when(jwtTokenProvider.getAuthentication(TOKEN)).thenReturn(null);

    jwtTokenFilter.doFilter(request, response, chain);

    verify(chain, only()).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void doFilterShouldCallChainDoFilterOnesAndSecurityContextShouldBeNullWhenTokenIsNotValid()
      throws ServletException, IOException {
    when(jwtTokenProvider.resolveToken(request)).thenReturn(TOKEN);
    when(jwtTokenProvider.validateToken(TOKEN)).thenReturn(false);

    jwtTokenFilter.doFilter(request, response, chain);

    verify(chain, only()).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void doFilterShouldCallChainDoFilterOnesAndContextOfSecurityContextHolderShouldNotNull()
      throws ServletException, IOException {
    final var authentication = mock(Authentication.class);

    when(jwtTokenProvider.resolveToken(request)).thenReturn(TOKEN);
    when(jwtTokenProvider.validateToken(TOKEN)).thenReturn(true);
    when(jwtTokenProvider.getAuthentication(TOKEN)).thenReturn(authentication);

    jwtTokenFilter.doFilter(request, response, chain);

    verify(chain, only()).doFilter(request, response);
    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
  }
}
