package com.illiapinchuk.moodle.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illiapinchuk.common.TestConstants;
import com.illiapinchuk.moodle.configuration.security.jwt.JwtTokenProvider;
import com.illiapinchuk.moodle.service.RedisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

  @Mock private RedisService redisService;
  @Mock private JwtTokenProvider jwtTokenProvider;
  private MockHttpServletRequest request;
  @InjectMocks private JwtServiceImpl jwtService;

  @Test
  void expireJwtToken_ShouldAddTokenToBlackList() {
    when(jwtTokenProvider.resolveToken(request))
        .thenReturn(TestConstants.AuthConstants.VALID_TOKEN);

    jwtService.expireJwtToken(request);

    verify(redisService).addTokenToBlackList(TestConstants.AuthConstants.VALID_TOKEN);
  }
}
