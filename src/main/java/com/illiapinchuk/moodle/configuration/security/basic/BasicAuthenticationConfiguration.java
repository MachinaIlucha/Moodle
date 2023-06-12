package com.illiapinchuk.moodle.configuration.security.basic;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Configuration class for basic authentication. Creates new beans for authentication manager and
 * authentication entry point.
 */
@Configuration
@RequiredArgsConstructor
public class BasicAuthenticationConfiguration {

  @Value("${basic.name}")
  private String swaggerName;

  @Value("${basic.role}")
  private String swaggerRole;

  private final PasswordEncoder passwordEncoder;

  private final JsonMapper jsonMapper;

  /**
   * Create bean for custom basic authentication entry point.
   *
   * @return custom basic authentication entry point
   */
  @Bean
  public AuthenticationEntryPoint basicAuthEntryPoint() {
    return new CustomBasicAuthEntryPoint(jsonMapper);
  }

  /**
   * Create bean for custom basic authentication manager.
   *
   * @return custom basic authentication manager.
   */
  @Bean
  public AuthenticationManager basicAuthenticationManager(
      @Value("${basic.password}") CharSequence swaggerPassword) {
    return new BasicAuthenticationManager(
        swaggerName, passwordEncoder.encode(swaggerPassword), swaggerRole, passwordEncoder);
  }
}
