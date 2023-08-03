package com.illiapinchuk.moodle.configuration.security.jwt;

import com.illiapinchuk.common.TestConstants;
import com.illiapinchuk.moodle.exception.InvalidJwtTokenException;
import com.illiapinchuk.moodle.exception.JwtTokenExpiredException;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.service.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderImplTest {

  @Mock private SecretProvider secretProvider;
  @Mock private UserDetailsService userDetailsService;
  @Mock private RedisService redisService;
  @Mock private MockHttpServletRequest mockHttpServletRequest;
  @InjectMocks private JwtTokenProviderImpl jwtTokenProvider;

  private static final Set<RoleName> ROLES = TestConstants.AuthConstants.ROLES_USER_ONLY;
  private static final String USER_EMAIL = TestConstants.UserConstants.USER_EMAIL;
  private static final String USER_LOGIN = TestConstants.UserConstants.USER_LOGIN;
  private static final String SECRET_KEY = TestConstants.AuthConstants.SECRET_KEY;
  private static final long VALIDITY_IN_MILLISECONDS =
      TestConstants.AuthConstants.VALIDITY_IN_MILLISECONDS;
  private static final String HEADER = TestConstants.AuthConstants.HEADER;
  private static final String INVALID_TOKEN = TestConstants.AuthConstants.INVALID_TOKEN;
  private static final String VALID_TOKEN = TestConstants.AuthConstants.VALID_TOKEN;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(
        jwtTokenProvider, "validityInMilliseconds", VALIDITY_IN_MILLISECONDS);
    when(secretProvider.getEncodedSecret()).thenReturn(SECRET_KEY);
  }

  @Test
  void createToken_ShouldReturnValidToken() {
    String token = createToken(USER_EMAIL);
    assertNotNull(token);
  }

  @Test
  void createToken_ShouldIncludeCorrectSubject() {
    final var token = createToken(USER_EMAIL);

    final var userEmailOrLogin = getSubjectFromToken(token);

    assertEquals(USER_EMAIL, userEmailOrLogin);
  }

  @Test
  void createToken_ShouldHaveExpirationTimeSet() {
    final var token = createToken(USER_EMAIL);

    final var expiration = getExpirationFromToken(token);

    assertNotNull(expiration);
    assertTrue(expiration.getTime() - System.currentTimeMillis() > 0);
  }

  @Test
  void createToken_ShouldUseCorrectSignatureAlgorithm() {
    final var token = createToken(USER_EMAIL);
    final var algorithm = getAlgorithmFromToken(token);

    assertEquals(SignatureAlgorithm.HS256, algorithm);
  }

  @Test
  void createToken_ShouldThrowException_WhenSecretProviderReturnsNull() {
    when(secretProvider.getEncodedSecret()).thenReturn(null);

    assertThrows(IllegalArgumentException.class, () -> createToken(USER_EMAIL));
  }

  @Test
  void validateToken_ShouldThrowInvalidJwtTokenException_WhenTokenIsExpired() {
    assertThrows(
        InvalidJwtTokenException.class, () -> jwtTokenProvider.validateToken(INVALID_TOKEN));
  }

  @Test
  void getAuthentication_ShouldReturnAuthenticationObjectWithRightData() {
    final var token = createToken(USER_EMAIL);
    final var userDetails = mock(UserDetails.class);

    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

    final var authentication = jwtTokenProvider.getAuthentication(token);

    assertNotNull(authentication);
    assertEquals(userDetails, authentication.getPrincipal());
    assertEquals("", authentication.getCredentials());
    assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
  }

  @Test
  void getAuthentication_ShouldThrowIllegalArgumentException_WhenTokenIsNull() {
    assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.getAuthentication(null));
  }

  @Test
  void getLoginOrEmail_ShouldReturnSameEmailAsWePassedToTheToken() {
    final var token = createToken(USER_EMAIL);

    final var usernameFromToken = jwtTokenProvider.getLoginOrEmail(token);

    assertEquals(USER_EMAIL, usernameFromToken);
  }

  @Test
  void getLoginOrEmail_ShouldReturnSameLoginAsWePassedToTheToken() {
    final var token = createToken(USER_LOGIN);

    final var loginFromToken = jwtTokenProvider.getLoginOrEmail(token);

    assertEquals(USER_LOGIN, loginFromToken);
  }

  @Test
  void getLoginOrEmail_ShouldThrowIllegalArgumentException_WhenTokenIsNull() {
    assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.getLoginOrEmail(null));
  }

  @Test
  void resolveToken_ShouldReturnResolvedToken() {
    lenient().when(secretProvider.getEncodedSecret()).thenReturn(SECRET_KEY);

    when(mockHttpServletRequest.getHeader(HEADER)).thenReturn(VALID_TOKEN);

    final var resolvedToken = jwtTokenProvider.resolveToken(mockHttpServletRequest);

    assertEquals(VALID_TOKEN, resolvedToken);
  }

  @Test
  void resolveToken_ShouldReturnNull_WhenTokenIsNull() {
    lenient().when(secretProvider.getEncodedSecret()).thenReturn(SECRET_KEY);

    when(mockHttpServletRequest.getHeader(HEADER)).thenReturn(null);

    assertNull(jwtTokenProvider.resolveToken(mockHttpServletRequest));
  }

  @Test
  void validateToken_ShouldReturnTrueIfTokenIsValid() {
    final var token = createToken(USER_LOGIN);

    // Mock redisService.isBlacklisted() to return false (not blacklisted)
    when(redisService.isBlacklisted(token)).thenReturn(false);

    boolean isValid = jwtTokenProvider.validateToken(token);

    assertTrue(isValid);
    verify(redisService, times(1)).isBlacklisted(token);
  }

  @Test
  void validateToken_ShouldThrowJwtTokenExpiredException_WhenTokenIsBlackListed() {
    lenient().when(secretProvider.getEncodedSecret()).thenReturn(SECRET_KEY);

    when(redisService.isBlacklisted(INVALID_TOKEN)).thenReturn(true);

    assertThrows(
        JwtTokenExpiredException.class, () -> jwtTokenProvider.validateToken(INVALID_TOKEN));
  }

  @Test
  void validateToken_ShouldThrowInvalidJwtTokenException_WhenTokenIsEmpty() {
    assertThrows(InvalidJwtTokenException.class, () -> jwtTokenProvider.validateToken(" "));
  }

  private String createToken(String subject) {
    return jwtTokenProvider.createToken(subject, ROLES);
  }

  private String getSubjectFromToken(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }

  private Date getExpirationFromToken(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
  }

  private SignatureAlgorithm getAlgorithmFromToken(String token) {
    return SignatureAlgorithm.valueOf(
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getHeader().getAlgorithm());
  }
}
