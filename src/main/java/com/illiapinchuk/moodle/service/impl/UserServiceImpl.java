package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.UserMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import com.illiapinchuk.moodle.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of {@link UserService} interface. */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserValidator userValidator;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Nullable
  public User getUserByLoginOrEmail(final String login, final String email) {
    final var userOptional = userRepository.findUserByLoginOrEmail(login, email);
    return userOptional.orElseThrow(
        () -> new UserNotFoundException("User with current login/email not found"));
  }

  @Override
  public User createUser(@NotNull final User user) {
    if (userValidator.isLoginExistInDb(user.getLogin())) {
      throw new EntityExistsException("User with current login already exists in the database.");
    }
    if (userValidator.isEmailExistInDb(user.getEmail())) {
      throw new EntityExistsException("User with current email already exists in the database.");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    // By default user role is USER
    user.setRoles(Set.of(RoleName.USER));
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
