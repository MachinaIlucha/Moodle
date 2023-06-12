package com.illiapinchuk.moodle.configuration.security;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.model.entity.RoleName;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Configuration class for Swagger endpoints' security. Implements {@link HttpSecurityConfig} to
 * configure security and apply to security filter chain.
 */
@Slf4j
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SwaggerSecurityConfig implements HttpSecurityConfig {

  private final AuthenticationEntryPoint basicAuthEntryPoint;

  private final AuthenticationManager basicAuthenticationManager;

  private static final String[] SWAGGER_WHITELIST = {
    ApplicationConstants.Web.Swagger.API_DOCS_PATH,
    ApplicationConstants.Web.Swagger.UI_PATH,
    ApplicationConstants.Web.Swagger.UI_HTML_PATH
  };

  /**
   * Swagger endpoints' security configuration.
   *
   * @return http security.
   */
  @Override
  public Consumer<HttpSecurity> configuration() {
    return http -> {
      try {
        http.authorizeHttpRequests()
            .requestMatchers(SWAGGER_WHITELIST)
            .hasAuthority(RoleName.DEVELOPER.name())
            .and()
            .httpBasic()
            .authenticationEntryPoint(basicAuthEntryPoint)
            .and()
            .authenticationManager(basicAuthenticationManager);
      } catch (Exception e) {
        log.error("Error in swagger security configuration: ", e);
      }
    };
  }
}
