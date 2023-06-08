package com.illiapinchuk.moodle.service.impl;

import static org.mockito.Mockito.verify;

import com.illiapinchuk.moodle.configuration.security.jwt.JwtTokenProvider;
import com.illiapinchuk.moodle.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

  @Mock
  private RedisService redisService;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @Mock
  private HttpServletRequest httpServletRequest;

  @InjectMocks
  private JwtServiceImpl jwtService;

  private static final String SAMPLE_TOKEN = "sampleToken";

  @Test
  void expireJwtToken_shouldAddTokenToBlacklist() {
    Mockito.when(jwtTokenProvider.resolveToken(httpServletRequest)).thenReturn(SAMPLE_TOKEN);

    jwtService.expireJwtToken(httpServletRequest);

    verify(jwtTokenProvider).resolveToken(httpServletRequest);
    verify(redisService).addTokenToBlackList(SAMPLE_TOKEN);
  }
}
