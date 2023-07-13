package com.illiapinchuk.moodle.configuration.security.jwt;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.exception.InvalidJwtTokenException;
import com.illiapinchuk.moodle.exception.JwtTokenExpiredException;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/** Class that provides methods for generation, validation, etc. of JWT token. */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {
  private final SecretProvider secretProvider;
  private final UserDetailsService userDetailsService;
  private final RedisService redisService;

  @Value("${spring.security.expirationTime}")
  private long validityInMilliseconds;

  @Override
  public String createToken(
      @Nonnull final String loginOrEmail, @Nonnull final Set<RoleName> roles) {
    final var claims = Jwts.claims().setSubject(loginOrEmail);
    claims.put(ApplicationConstants.Web.Security.JwtClaims.ROLES, getRoleNames(roles));

    claims.put(
        ApplicationConstants.Web.Security.JwtClaims.TIME_ZONE_ID,
        ApplicationConstants.Web.Security.SERVER_TIMEZONE_ID);

    var now =
        getCurrentZonedDateTime(
            claims.get(ApplicationConstants.Web.Security.JwtClaims.TIME_ZONE_ID).toString());
    final var expirationDate = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS256, secretProvider.getEncodedSecret())
        .compact();
  }

  @Override
  public Authentication getAuthentication(@Nonnull final String token) {
    final var userDetails = this.userDetailsService.loadUserByUsername(getLoginOrEmail(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  @Override
  public String getLoginOrEmail(@Nonnull final String token) {
    return Jwts.parser()
        .setSigningKey(secretProvider.getEncodedSecret())
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  @Override
  public String resolveToken(@Nonnull final HttpServletRequest request) {
    return request.getHeader(ApplicationConstants.Web.Security.TOKEN_HEADER_NAME);
  }

  @Override
  public boolean validateToken(@Nonnull final String token) {
    if (redisService.isBlacklisted(token)) {
      log.error("Jwt token was expired.");
      throw new JwtTokenExpiredException("Jwt token was expired and can't be used again.");
    }

    try {
      final var claims =
          Jwts.parser().setSigningKey(secretProvider.getEncodedSecret()).parseClaimsJws(token);
      final var timeZoneId =
          claims.getBody().get(ApplicationConstants.Web.Security.JwtClaims.TIME_ZONE_ID).toString();

      return !claims.getBody().getExpiration().before(getCurrentZonedDateTime(timeZoneId));
    } catch (SignatureException e) {
      log.error("Invalid JWT signature.", e);
      throw new InvalidJwtTokenException("Invalid JWT signature.");
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token.", e);
      throw new InvalidJwtTokenException("Invalid JWT token.");
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token.", e);
      throw new InvalidJwtTokenException("Expired JWT token.");
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token.", e);
      throw new InvalidJwtTokenException("Unsupported JWT token.");
    } catch (IllegalArgumentException e) {
      log.error("JWT token compact of handler are invalid.", e);
      throw new InvalidJwtTokenException("JWT token compact of handler are invalid.");
    }
  }

  /**
   * Converts a set of RoleName enums to a set of strings.
   *
   * @param userRoles the set of RoleName enums to convert
   * @return a set of strings containing the string representation of each RoleName
   */
  private Set<String> getRoleNames(@Nonnull final Set<RoleName> userRoles) {
    final var result = new HashSet<String>();

    userRoles.forEach(role -> result.add(role.toString()));

    return result;
  }

  /**
   * Returns the current date and time in the specified time zone.
   *
   * @param timeZoneId the ID of the time zone to use
   * @return the current date and time in the specified time zone
   */
  private Date getCurrentZonedDateTime(@Nonnull final String timeZoneId) {
    return Date.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(timeZoneId)).toInstant());
  }
}
