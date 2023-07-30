package com.illiapinchuk.moodle.configuration.security.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@ExtendWith(MockitoExtension.class)
class JwtConfigurerTest {

  @Mock private JwtTokenProvider jwtTokenProvider;

  @Mock private HttpSecurity httpSecurity;

  @InjectMocks private JwtConfigurer jwtConfigurer;

  @Test
  void testConfigure() {
    final var mockHttpSecurity = mock(HttpSecurity.class);
    jwtConfigurer.configure(mockHttpSecurity);

    verify(mockHttpSecurity, times(1))
        .addFilterBefore(any(JwtTokenFilter.class), eq(UsernamePasswordAuthenticationFilter.class));
  }
}
