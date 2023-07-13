package com.illiapinchuk.moodle.configuration.security.jwt;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.exception.JwtTokenExpiredException;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.service.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderImplTest {

  @Mock private SecretProvider secretProvider;
  @Mock private UserDetailsService userDetailsService;
  @Mock private RedisService redisService;
  @InjectMocks private JwtTokenProviderImpl jwtTokenProvider;

  @Test
  void createToken_ShouldReturnValidToken() {
    final var roles = new HashSet<>(Collections.singletonList(RoleName.USER));

    when(secretProvider.getEncodedSecret()).thenReturn(TestConstants.AuthConstants.SECRET_KEY);

    String token = jwtTokenProvider.createToken(TestConstants.UserConstants.USER_EMAIL, roles);

    assertNotNull(token);
  }

  @Test
  void createToken_ShouldThrowException_WhenLoginOrEmailIsNull() {
    final var roles = new HashSet<>(Collections.singletonList(RoleName.USER));

    when(secretProvider.getEncodedSecret()).thenReturn(TestConstants.AuthConstants.SECRET_KEY);

    assertThrows(NullPointerException.class, () -> jwtTokenProvider.createToken(null, roles));
  }

  @Test
  void createToken_ShouldThrowException_WhenRolesIsNull() {
    String loginOrEmail = "testUser";

    when(secretProvider.getEncodedSecret()).thenReturn("testSecret");

    assertThrows(
        NullPointerException.class, () -> jwtTokenProvider.createToken(loginOrEmail, null));
  }

  @Test
  void createToken_ShouldIncludeCorrectClaims() {
    String loginOrEmail = "testUser";
    Set<RoleName> roles = new HashSet<>(Collections.singletonList(RoleName.USER));

    when(secretProvider.getEncodedSecret()).thenReturn("testSecret");

    String token = jwtTokenProvider.createToken(loginOrEmail, roles);

    assertNotNull(token);
    assertTrue(token.contains(loginOrEmail));
    assertTrue(token.contains(RoleName.USER.toString()));
  }

  @Test
  void createToken_ShouldHaveExpirationTimeSet() {
    // Arrange
    String loginOrEmail = "testUser";
    Set<RoleName> roles = new HashSet<>(Collections.singletonList(RoleName.USER));

    // Mock secretProvider
    when(secretProvider.getEncodedSecret()).thenReturn("testSecret");

    // Act
    String token = jwtTokenProvider.createToken(loginOrEmail, roles);
    Date expiration =
        Jwts.parser().setSigningKey("testSecret").parseClaimsJws(token).getBody().getExpiration();

    // Assert
    assertNotNull(expiration);
    assertTrue(expiration.getTime() - System.currentTimeMillis() > 0);
  }

  @Test
  void createToken_ShouldUseCorrectSignatureAlgorithm() {
    // Arrange
    String loginOrEmail = "testUser";
    Set<RoleName> roles = new HashSet<>(Collections.singletonList(RoleName.USER));

    // Mock secretProvider
    when(secretProvider.getEncodedSecret()).thenReturn("testSecret");

    // Act
    String token = jwtTokenProvider.createToken(loginOrEmail, roles);
    SignatureAlgorithm algorithm =
        SignatureAlgorithm.valueOf(
            Jwts.parser()
                .setSigningKey("testSecret")
                .parseClaimsJws(token)
                .getHeader()
                .getAlgorithm());

    // Assert
    assertEquals(SignatureAlgorithm.HS256, algorithm);
  }

  @Test
  void createToken_ShouldThrowException_WhenSecretProviderReturnsNull() {
    // Arrange
    String loginOrEmail = "testUser";
    Set<RoleName> roles = new HashSet<>(Collections.singletonList(RoleName.USER));

    // Mock secretProvider
    when(secretProvider.getEncodedSecret()).thenReturn(null);

    // Act and Assert
    assertThrows(
        NullPointerException.class, () -> jwtTokenProvider.createToken(loginOrEmail, roles));
  }

  //  @Test
  //  void createToken_ShouldHandleExpiredToken() {
  //    // Arrange
  //    String loginOrEmail = "testUser";
  //    Set<RoleName> roles = new HashSet<>(Collections.singletonList(RoleName.USER));
  //
  //    // Mock secretProvider
  //    when(secretProvider.getEncodedSecret()).thenReturn("testSecret");
  //
  //    // Act
  //    String token = jwtTokenProvider.createToken(loginOrEmail, roles);
  //
  //    // Simulate token expiration by setting the current time to a past time
  //    LocalDateTime currentTime = LocalDateTime.now().minusDays(1);
  //    jwtTokenProvider.validityInMilliseconds =
  //        1000; // Set a short validity time for testing purposes
  //
  //    // Assert
  //    assertThrows(JwtTokenExpiredException.class, () -> jwtTokenProvider.validateToken(token));
  //  }
}
