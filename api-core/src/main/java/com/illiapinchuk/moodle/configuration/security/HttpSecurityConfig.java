package com.illiapinchuk.moodle.configuration.security;

import java.util.function.Consumer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/** Interface for additional security configuration. */
public interface HttpSecurityConfig {
  Consumer<HttpSecurity> configuration();
}
