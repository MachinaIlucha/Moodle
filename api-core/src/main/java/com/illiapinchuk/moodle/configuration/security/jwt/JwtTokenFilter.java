package com.illiapinchuk.moodle.configuration.security.jwt;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/** JWT token filter that handles all HTTP requests to application. */
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilter(
      @Nonnull final ServletRequest request,
      @Nonnull final ServletResponse response,
      @Nonnull final FilterChain chain)
      throws IOException, ServletException {
    final var token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

    if (token != null && jwtTokenProvider.validateToken(token)) {
      final var authentication = jwtTokenProvider.getAuthentication(token);

      if (authentication != null) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Successfully authenticated user with JWT token");
      }
    }

    chain.doFilter(request, response);
  }
}
