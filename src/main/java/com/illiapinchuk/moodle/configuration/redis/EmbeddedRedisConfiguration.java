package com.illiapinchuk.moodle.configuration.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.embedded.RedisServer;

@Configuration
public class EmbeddedRedisConfiguration {

  private final RedisServer redisServer;

  @Value("${spring.data.redis.host}")
  private String redisHost;

  public EmbeddedRedisConfiguration(@Value("${spring.data.redis.port}") int redisPort) {
    this.redisServer = RedisServer.builder().port(redisPort).setting("maxheap 128M").build();
  }

  @PostConstruct
  public void postConstruct() {
    redisServer.start();
  }

  @PreDestroy
  public void preDestroy() {
    redisServer.stop();
  }

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    final var redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisServer.ports().get(0));
    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }
}
