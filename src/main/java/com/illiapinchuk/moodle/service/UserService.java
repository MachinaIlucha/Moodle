package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.persistence.entity.User;

/**
 * Service interface for managing users.
 */
public interface UserService {

  /**
   * Returns a User object by his username from database for later authentication.
   *
   * @param login - login of user to find
   * @return the user object of specified username
   * @see User
   */
  User getUserByLoginOrEmail(final String login, final String email);

  /**
   * Creates a new user.
   *
   * @param user the user to create
   * @return the created user
   * @see User
   */
  User createUser(final User user);

  /**
   * Updates the user information based on the provided UserDto object.
   *
   * @param userDto The UserDto object containing the updated user information.
   * @return The updated User object.
   */
  User updateUser(final UserDto userDto);

  /**
   * Deletes a user by login or email.
   *
   * @param login the login of the user to delete
   * @param email the email of the user to delete
   */
  void deleteUserByLoginOrEmail(final String login, final String email);
}
