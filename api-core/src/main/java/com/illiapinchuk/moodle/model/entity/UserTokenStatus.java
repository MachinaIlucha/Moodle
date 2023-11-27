package com.illiapinchuk.moodle.model.entity;

/**
 * Enumeration representing the status of a user token.
 *
 * <p>It can have one of the following values:
 *
 * <ul>
 *   <li>{@link #WAITING}: The token is waiting to be used.
 *   <li>{@link #USED}: The token has been used.
 * </ul>
 */
public enum UserTokenStatus {
  /** The token is waiting to be used. */
  WAITING,

  /** The token has been used. */
  USED;
}
