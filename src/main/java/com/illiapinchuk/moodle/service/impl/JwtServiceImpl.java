package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.configuration.security.jwt.JwtTokenProvider;
import com.illiapinchuk.moodle.service.JwtService;
import com.illiapinchuk.moodle.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** The JwtServiceImpl class is an implementation of {@link JwtService} interface. */
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final RedisService redisService;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void expireJwtToken(@NotNull final HttpServletRequest request) {
    final var token = jwtTokenProvider.resolveToken(request);
    redisService.addTokenToBlackList(token);
  }
}
