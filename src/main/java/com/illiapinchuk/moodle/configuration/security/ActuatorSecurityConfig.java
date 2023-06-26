package com.illiapinchuk.moodle.configuration.security;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.model.entity.RoleName;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Configuration class for Actuator endpoints security. Implements {@link HttpSecurityConfig} to
 * configure security and apply to security filter chain.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ActuatorSecurityConfig implements HttpSecurityConfig {

  /**
   * Spring Actuator security configuration.
   *
   * @return http security.
   */
  @Override
  public Consumer<HttpSecurity> configuration() {
    return http -> {
      try {
        http.authorizeHttpRequests()
            .requestMatchers(ApplicationConstants.Web.Path.Actuator.EXTENDED_MAIN_PATH)
            .hasAuthority(RoleName.DEVELOPER.name())
            .and()
            .cors()
            .configure(http);
      } catch (Exception e) {
        log.error("Error in actuator security configuration: ", e);
      }
    };
  }
}
