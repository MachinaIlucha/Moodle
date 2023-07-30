package com.illiapinchuk.moodle.configuration.security.jwt;

import com.illiapinchuk.moodle.model.entity.RoleName;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import org.springframework.security.core.Authentication;

/**
 * The JwtTokenProvider interface to provide methods for creating, validating, and manipulating JWT
 * tokens.
 */
public interface JwtTokenProvider {

  /**
   * Creates a new JWT token with the given username and roles.
   *
   * @param loginOrEmail the login or email to include in the token
   * @param roles the set of roles to include in the token
   * @return the created JWT token
   */
  String createToken(String loginOrEmail, Set<RoleName> roles);

  /**
   * Returns the authentication information contained in the given JWT token.
   *
   * @param token the JWT token to extract the authentication information from
   * @return the authentication information contained in the token
   */
  Authentication getAuthentication(String token);

  /**
   * Returns the login or email contained in the given JWT token.
   *
   * @param token the JWT token to extract the login or email from
   * @return the login or email contained in the token
   */
  String getLoginOrEmail(String token);

  /**
   * Resolves the JWT token contained in the given HTTP servlet request.
   *
   * @param request the HTTP servlet request to extract the JWT token from
   * @return the resolved JWT token, or null if no token was found
   */
  String resolveToken(HttpServletRequest request);

  /**
   * Validates the given JWT token.
   *
   * @param token the JWT token to validate
   * @return true if the token is valid, false otherwise
   */
  boolean validateToken(String token);
}
