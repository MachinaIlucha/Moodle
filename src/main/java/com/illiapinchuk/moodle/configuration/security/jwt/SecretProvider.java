package com.illiapinchuk.moodle.configuration.security.jwt;

/** An interface for providing encoded secrets. */
public interface SecretProvider {

  /**
   * Returns an encoded secret.
   *
   * @return the encoded secret
   */
  String getEncodedSecret();
}
