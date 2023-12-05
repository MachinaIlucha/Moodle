package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.service.business.RedisService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/** The RedisServiceImpl class is an implementation of the {@link RedisService} interface. */
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void addTokenToBlackList(@Nonnull final String token) {
    redisTemplate.opsForValue().set(token, "blacklisted");
  }

  @Override
  public boolean isBlacklisted(@Nonnull final String token) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(token));
  }
}
