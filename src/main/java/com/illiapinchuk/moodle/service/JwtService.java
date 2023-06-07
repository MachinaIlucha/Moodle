package com.illiapinchuk.moodle.service;

import jakarta.servlet.http.HttpServletRequest;

/** The JwtService interface provides methods for managing JWT tokens. */
public interface JwtService {

  /**
   * Expires a JWT token.
   *
   * @param request The HttpServletRequest containing the JWT token to be expired.
   */
  void expireJwtToken(HttpServletRequest request);
}
