package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.persistence.entity.User;

/** Service interface for managing users. */
public interface UserService {

  /**
   * Returns a User object by his username from database for later authentication.
   *
   * @param login - login of user to find
   * @return the user object of specified username
   * @see User
   */
  User getUserByLoginOrEmail(String login, String email);

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The user object associated with the provided ID.
   */
  User getUserById(Long id);

  /**
   * Creates a new user.
   *
   * @param user the user to create
   * @return the created user
   * @see User
   */
  User createUser(User user);

  /**
   * Updates the user information based on the provided UserDto object.
   *
   * @param userDto The UserDto object containing the updated user information.
   * @return The updated User object.
   */
  User updateUser(UserDto userDto);

  /**
   * Updates the password for a user with the specified ID.
   *
   * @param password The new password for the user. Should not be null or empty.
   * @param id The unique ID of the user whose password needs to be updated. Should be a positive
   *     value.
   */
  void updateUserPassword(String password, Long id);

  /**
   * Deletes a user by login or email.
   *
   * @param login the login of the user to delete
   * @param email the email of the user to delete
   */
  void deleteUserByLoginOrEmail(String login, String email);
}
