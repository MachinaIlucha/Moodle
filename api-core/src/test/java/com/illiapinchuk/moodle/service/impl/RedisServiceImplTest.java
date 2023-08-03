package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisServiceImplTest {

  @Mock private RedisTemplate<String, Object> redisTemplate;
  @Mock private ValueOperations<String, Object> valueOperations;
  @InjectMocks private RedisServiceImpl redisService;

  @Test
  void addTokenToBlackList_ShouldAddTokenToRedisTemplate() {
    // Mock the opsForValue() method to return the ValueOperations mock
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    redisService.addTokenToBlackList(TestConstants.AuthConstants.INVALID_TOKEN);

    verify(valueOperations, times(1)).set(eq(TestConstants.AuthConstants.INVALID_TOKEN), eq("blacklisted"));
  }

  @Test
  void isBlacklisted_WhenTokenIsBlacklisted_ShouldReturnTrue() {
    when(redisTemplate.hasKey(TestConstants.AuthConstants.INVALID_TOKEN)).thenReturn(true);

    assertTrue(redisService.isBlacklisted(TestConstants.AuthConstants.INVALID_TOKEN));
  }

  @Test
  void isBlacklisted_WhenTokenIsNotBlacklisted_ShouldReturnFalse() {
    when(redisTemplate.hasKey(TestConstants.AuthConstants.INVALID_TOKEN)).thenReturn(false);

    assertFalse(redisService.isBlacklisted(TestConstants.AuthConstants.INVALID_TOKEN));
  }
}
