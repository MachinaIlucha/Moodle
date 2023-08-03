package com.illiapinchuk.moodle.configuration.security.basic;

import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Custom authentication manager for basic authentication. Can be used for specific
 * SecurityFilterChain.
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAuthenticationManager implements AuthenticationManager {

  @NonNull private final String authenticationName;

  @NonNull private final String authenticationPassword;

  @NonNull private final String authenticationRole;

  @NonNull private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final var username = (String) authentication.getPrincipal();
    final var password = (String) authentication.getCredentials();
    log.info("basic authentication manager: authenticate user: {}", username);
    if (!passwordEncoder.matches(password, authenticationPassword)
        || !StringUtils.equals(username, authenticationName)) {
      throw new BadCredentialsException(" incorrect username or password ");
    }
    log.info("basic authentication manager: successfully authenticated user: {}", username);
    return new UsernamePasswordAuthenticationToken(
        username, password, Set.of(new SimpleGrantedAuthority(authenticationRole)));
  }
}
