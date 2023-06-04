package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.UserMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import com.illiapinchuk.moodle.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserService} interface.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserValidator userValidator;

  @Override
  @Nullable
  public User getUserByLoginOrEmail(final String login, final String email) {
    return userRepository.findUserByLoginOrEmail(login, email)
        .orElseThrow(() -> new UserNotFoundException("User with current login/email not found"));
  }

  @Override
  public User createUser(@NotNull final User user) {
    final var userLogin = user.getLogin();
    final var userEmail = user.getEmail();
    if (userValidator.isLoginExistInDb(userLogin)) {
      throw new EntityExistsException("User with current login already exist in db.");
    }
    if (userValidator.isEmailExistInDb(userEmail)) {
      throw new EntityExistsException("User with current email already exist in db.");
    }

    return userRepository.save(user);
  }

  @Override
  public User updateUser(@NotNull final UserDto userDto) {
    final var userLogin = userDto.getLogin();
    if (!userValidator.isLoginExistInDb(userLogin)) {
      throw new UserNotFoundException("User with current login not found");
    }
    final var user = getUserByLoginOrEmail(userLogin, null);
    userMapper.updateUser(user, userDto);

    return userRepository.save(user);
  }

  @Override
  @Transactional
  public void deleteUserByLoginOrEmail(final String login, final String email) {
    if (!userValidator.isLoginExistInDb(login) && userValidator.isEmailExistInDb(email)) {
      throw new UserNotFoundException("User with current login/email not found");
    }
    userRepository.deleteUserByLoginOrEmail(login, email);
  }
}
