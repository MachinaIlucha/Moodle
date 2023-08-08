package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.UserMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.UserCantModifyAnotherUserException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import com.illiapinchuk.moodle.service.UserService;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityExistsException;
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
  public User getUserByLoginOrEmail(final String login, final String email) {
    final var userOptional = userRepository.findUserByLoginOrEmail(login, email);
    return userOptional.orElseThrow(
        () -> new UserNotFoundException("User with current login/email not found"));
  }

  @Override
  public User getUserById(@Nonnull final Long id) {
    return userRepository.getReferenceById(id);
  }

  @Override
  public User createUser(@Nonnull final User user) {
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
  public User updateUser(@Nonnull final UserDto userDto) {
    final var userId = userDto.getId();

    if (!userValidator.isUserExistInDbById(userId)) {
      throw new UserNotFoundException("User with current id not found");
    }

    // If the authenticated user is not an admin and (does not have a USER role or is not updating
    // their own details)
    if (!UserPermissionService.hasAdminRole()
        && (!UserPermissionService.hasUserRole()
            || !UserPermissionService.isSameUserAsAuthenticated(userId))) {
      throw new UserCantModifyAnotherUserException("User has no permission to modify another user");
    }

    final var user = getUserById(userId);

    // Mapping the provided user details (from DTO) to the fetched user entity
    userMapper.updateUser(user, userDto);

    // Encoding and setting the password of the user entity
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Saving the updated user entity to the database and returning the saved entity
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
