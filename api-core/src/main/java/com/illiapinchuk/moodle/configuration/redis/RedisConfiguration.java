package com.illiapinchuk.moodle.configuration.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/** Configuration class for Redis connection and RedisTemplate setup. */
@Configuration
public class RedisConfiguration {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  /**
   * Creates a JedisConnectionFactory bean for establishing a connection to Redis.
   *
   * @return JedisConnectionFactory - the configured JedisConnectionFactory instance
   */
  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration =
        new RedisStandaloneConfiguration(redisHost, redisPort);
    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  /**
   * Creates a RedisTemplate bean for performing Redis operations.
   *
   * @return RedisTemplate<String, Object> - the configured RedisTemplate instance
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }
}
