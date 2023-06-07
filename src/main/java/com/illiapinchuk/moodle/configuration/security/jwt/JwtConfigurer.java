package com.illiapinchuk.moodle.configuration.security.jwt;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** JWT configuration for application that add {@link JwtTokenFilter} for security chain. */
@RequiredArgsConstructor
public class JwtConfigurer
    extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void configure(@NotNull final HttpSecurity httpSecurity) {
    final var jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
    httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
