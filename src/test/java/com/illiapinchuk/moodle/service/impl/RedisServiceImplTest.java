package com.illiapinchuk.moodle.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceImplTest {

  private static final String TOKEN = "testToken";
  private static final String BLACKLISTED = "blacklisted";

  @Mock private RedisTemplate<String, Object> redisTemplate;

  @InjectMocks private RedisServiceImpl redisService;

  @Test
  void addTokenToBlackListTest() {
    final var valueOperations = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    redisService.addTokenToBlackList(TOKEN);

    verify(redisTemplate.opsForValue(), times(1)).set(TOKEN, BLACKLISTED);
  }

  @Test
  void isBlacklistedTest() {
    when(redisTemplate.hasKey(TOKEN)).thenReturn(true);
    final var result = redisService.isBlacklisted(TOKEN);

    verify(redisTemplate, times(1)).hasKey(TOKEN);
    assert (result);
  }
}
