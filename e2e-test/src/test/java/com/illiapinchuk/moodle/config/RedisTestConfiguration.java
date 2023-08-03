package com.illiapinchuk.moodle.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

@TestConfiguration
public class RedisTestConfiguration {

  @Value("${spring.data.redis.port}")
  private int redisPort;

  private RedisServer redisServer;

  @PostConstruct
  public void startRedis() {
    this.redisServer =
        RedisServer.builder()
            .port(redisPort)
            .setting("maxmemory 128M")
            .build();
    this.redisServer.start();
  }

  @PreDestroy
  public void stopRedis() {
      this.redisServer.stop();
  }
}
