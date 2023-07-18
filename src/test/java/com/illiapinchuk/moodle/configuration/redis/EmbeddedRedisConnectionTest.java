package com.illiapinchuk.moodle.configuration.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@Import(RedisConfiguration.class)
class EmbeddedRedisConnectionTest {
  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Test
  void testRedisConnection() {
    // Check connection to Redis
    assertThat(redisTemplate).isNotNull();
  }
}
