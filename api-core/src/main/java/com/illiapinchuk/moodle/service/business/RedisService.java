package com.illiapinchuk.moodle.service.business;

/** The RedisService interface provides methods to interact with Redis for token management. */
public interface RedisService {

  /**
   * Adds a token to the blacklist.
   *
   * @param token The token to be added to the blacklist.
   */
  void addTokenToBlackList(String token);

  /**
   * Checks if a token is blacklisted.
   *
   * @param token The token to check.
   * @return {@code true} if the token is blacklisted, {@code false} otherwise.
   */
  boolean isBlacklisted(String token);
}
