package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.entity.UserToken;

/**
 * Service interface for managing UserToken entities.
 *
 * <p>This interface defines methods for handling user tokens, including saving tokens, deleting
 * tokens by user ID, and retrieving a user by their token.
 */
public interface UserTokenService {

  /**
   * Saves a UserToken entity.
   *
   * @param userToken The UserToken to be saved.
   */
  void saveToken(UserToken userToken);

  /**
   * Deletes a UserToken associated with a specific user by their user ID.
   *
   * @param userId The ID of the user whose token should be deleted.
   */
  void deleteTokenByUserId(Long userId);

  /**
   * Retrieves a User entity associated with a given token.
   *
   * @param token The token for which to retrieve the associated User.
   * @return The User entity associated with the token, or null if not found.
   */
  User getUserByToken(String token);

  /**
   * Sets the status of a user token to "USED" based on the provided token.
   *
   * <p>This method updates the status of a user token to indicate that it has been used.
   *
   * @param token The token to be marked as "USED."
   */
  void setTokenUsed(String token);

  /** Deletes the tokens that have been used and are no longer needed. */
  void deleteUsedTokens();
}
