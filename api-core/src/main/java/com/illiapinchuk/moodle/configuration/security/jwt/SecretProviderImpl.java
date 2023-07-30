package com.illiapinchuk.moodle.configuration.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Implementation of {@link SecretProvider} interface. */
@Component
@Getter
public class SecretProviderImpl implements SecretProvider {

  private final String encodedSecret;

  public SecretProviderImpl(@Value("${spring.security.secret}") String secret) {
    this.encodedSecret =
        Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
  }
}
