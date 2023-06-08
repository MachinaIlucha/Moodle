package com.illiapinchuk.moodle.configuration.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.illiapinchuk.moodle.exception.InvalidJwtTokenException;
import com.illiapinchuk.moodle.exception.JwtTokenExpiredException;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.service.RedisService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
class JwtTokenProviderImplTest {

  private static final String VALID_TOKEN = "valid_token";
  private static final String INVALID_TOKEN = "invalidToken";
  private static final String USERNAME = "testUsername";
  private static final Set<RoleName> ROLES = Set.of(RoleName.USER);
  private static final String HEADER = "token";

  @MockBean private MockHttpServletRequest mockHttpServletRequest;
  @MockBean private UserDetailsService userDetailsService;
  @MockBean private UserDetails userDetails;
  @MockBean private RedisService redisService;
  @Autowired private JwtTokenProviderImpl jwtTokenProvider;

  @Test
  void createTokenWhenPassValidDataShouldReturnValidToken() {
    final var token = jwtTokenProvider.createToken(USERNAME, ROLES);

    final var isTokenValid = jwtTokenProvider.validateToken(token);

    assertTrue(isTokenValid);
  }

  @Test
  void createTokenShouldReturnNullPointerExceptionWhenRolesIsNull() {
    assertThrows(NullPointerException.class, () -> jwtTokenProvider.createToken(USERNAME, null));
  }

  @Test
  void getAuthenticationShouldReturnAuthenticationObjectWithRightData() {
    final var token = jwtTokenProvider.createToken(USERNAME, ROLES);
    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

    final var authentication = jwtTokenProvider.getAuthentication(token);

    assertNotNull(authentication);
    assertEquals(userDetails, authentication.getPrincipal());
    assertEquals("", authentication.getCredentials());
    assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
  }

  @Test
  void getAuthenticationShouldThrowIllegalArgumentExceptionWhenTokenIsNull() {
    assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.getAuthentication(null));
  }

  @Test
  void getUsernameShouldReturnSameUsernameAsWePassedToTheToken() {
    final var token = jwtTokenProvider.createToken(USERNAME, ROLES);

    final var usernameFromToken = jwtTokenProvider.getLoginOrEmail(token);

    assertEquals(USERNAME, usernameFromToken);
  }

  @Test
  void getUsernameShouldThrowIllegalArgumentExceptionWhenTokenIsNull() {
    assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.getLoginOrEmail(null));
  }

  @Test
  void resolveTokenWithBearerTokenShouldReturnResolvedToken() {
    when(mockHttpServletRequest.getHeader(HEADER)).thenReturn(VALID_TOKEN);

    final var resolvedToken = jwtTokenProvider.resolveToken(mockHttpServletRequest);

    assertEquals(VALID_TOKEN, resolvedToken);
  }

  @Test
  void resolveTokenWithNullTokenShouldReturnNull() {
    when(mockHttpServletRequest.getHeader(HEADER)).thenReturn(null);
    assertNull(jwtTokenProvider.resolveToken(mockHttpServletRequest));
  }

  @Test
  void validateTokenShouldThrowInvalidJwtTokenExceptionWhenSignatureIsInvalid() {
    final var invalidSignatureToken = "invalid_signature_token";

    assertThrows(
        InvalidJwtTokenException.class,
        () -> jwtTokenProvider.validateToken(invalidSignatureToken));
  }

  @Test
  void validateTokenShouldThrowInvalidJwtTokenExceptionWhenTokenIsMalformed() {
    final var malformedToken = "malformed_token";

    assertThrows(
        InvalidJwtTokenException.class, () -> jwtTokenProvider.validateToken(malformedToken));
  }

  @Test
  void validateTokenShouldThrowInvalidJwtTokenExceptionWhenTokenIsUnsupported() {
    final var unsupportedToken = "unsupported_token";

    assertThrows(
        InvalidJwtTokenException.class, () -> jwtTokenProvider.validateToken(unsupportedToken));
  }

  @Test
  void validateTokenShouldThrowInvalidJwtTokenExceptionWhenTokenHasInvalidCompact() {
    final var invalidCompactToken = "invalid_compact_token";

    assertThrows(
        InvalidJwtTokenException.class, () -> jwtTokenProvider.validateToken(invalidCompactToken));
  }

  @Test
  void validateInvalidTokenTest() {
    when(redisService.isBlacklisted(INVALID_TOKEN)).thenReturn(true);

    assertThrows(JwtTokenExpiredException.class, () -> {
      jwtTokenProvider.validateToken(INVALID_TOKEN);
    });
  }
}
