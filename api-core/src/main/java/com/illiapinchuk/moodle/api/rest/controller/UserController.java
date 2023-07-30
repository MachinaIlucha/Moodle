package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.common.mapper.UserMapper;
import com.illiapinchuk.moodle.exception.NotValidInputException;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for user. */
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  /**
   * Retrieves a user with the given login or email.
   *
   * @param login the login of the user to retrieve
   * @param email the email of the user to retrieve
   * @return a {@link ResponseEntity} containing the {@link UserDto} object and an HTTP status code
   */
  @GetMapping
  public ResponseEntity<UserDto> getUserByLoginOrEmail(
      @RequestParam(name = "login", required = false) final String login,
      @RequestParam(name = "email", required = false) final String email) {

    if (StringUtils.hasText(login) && StringUtils.hasText(email)) {
      throw new NotValidInputException("Either login or email must be provided");
    }

    final var user = userService.getUserByLoginOrEmail(login, email);

    final var userResponse = userMapper.userToUserDto(user);

    log.info("User with login: {} or email: {} was found", login, email);
    return ResponseEntity.ok(userResponse);
  }

  /**
   * Registers a new user by creating a new {@link User} with the information provided in the
   * request body.
   *
   * @param userDto a {@link UserDto} object containing the user's information
   * @return a {@link ResponseEntity} object with a status of 201 (Created) and the created {@link
   *     UserDto} object in the body.
   */
  @PostMapping
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestBody final UserDto userDto) {
    final var userRequest = userMapper.userDtoToUser(userDto);
    final var user = userService.createUser(userRequest);
    final var userResponse = userMapper.userToUserDto(user);

    log.info("New user was created with id: {}", user.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
  }

  /**
   * Update an existing user.
   *
   * @param userDto A {@link UserDto} object representing the updated user information.
   * @return A {@link ResponseEntity} containing the updated {@link UserDto} object.
   */
  @PutMapping
  public ResponseEntity<UserDto> updateUser(@Valid @RequestBody final UserDto userDto) {
    final var user = userService.updateUser(userDto);
    final var userResponse = userMapper.userToUserDto(user);

    log.info("User with id: {} was updated", user.getId());
    return ResponseEntity.ok().body(userResponse);
  }

  /**
   * Deletes the user with the given login.
   *
   * @param login the login of the user to delete
   * @return a response with an HTTP status of OK if the user was successfully deleted, or an HTTP
   *     status of NOT_FOUND if the user was not found
   */
  @DeleteMapping
  public ResponseEntity<Void> deleteUserByLoginOrEmail(
      @RequestParam(name = "login", required = false) final String login,
      @RequestParam(name = "email", required = false) final String email) {
    userService.deleteUserByLoginOrEmail(login, email);
    log.info("User with login: {} or email: {} was deleted", login, email);
    return ResponseEntity.ok().build();
  }
}
