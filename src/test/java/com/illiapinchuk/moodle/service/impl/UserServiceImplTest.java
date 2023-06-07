package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.mapper.UserMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  private static final String EXISTING_LOGIN = "existing_login";
  private static final String NON_EXISTING_LOGIN = "non_existing_login";
  private static final String EXISTING_EMAIL = "existing_email@example.com";
  private static final String NON_EXISTING_EMAIL = "non_existing_email@example.com";

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @Mock private UserValidator userValidator;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private UserServiceImpl userService;

  @Test
  void testGetUserByLoginOrEmail_WhenUserExists_ShouldReturnUser() {
    final var expectedUser = new User();
    when(userRepository.findUserByLoginOrEmail(EXISTING_LOGIN, EXISTING_EMAIL))
        .thenReturn(Optional.of(expectedUser));

    final var result = userService.getUserByLoginOrEmail(EXISTING_LOGIN, EXISTING_EMAIL);

    assertSame(expectedUser, result);
    verify(userRepository, times(1)).findUserByLoginOrEmail(EXISTING_LOGIN, EXISTING_EMAIL);
  }

  @Test
  void testGetUserByLoginOrEmail_WhenUserDoesNotExist_ShouldThrowException() {
    when(userRepository.findUserByLoginOrEmail(NON_EXISTING_LOGIN, NON_EXISTING_EMAIL))
        .thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> userService.getUserByLoginOrEmail(NON_EXISTING_LOGIN, NON_EXISTING_EMAIL));
    verify(userRepository, times(1)).findUserByLoginOrEmail(NON_EXISTING_LOGIN, NON_EXISTING_EMAIL);
  }

  @Test
  @WithMockUser(roles = "USER")
  void testCreateUser_WhenUserDoesNotExist_ShouldSaveUser() {
    final var user = new User();
    when(userValidator.isLoginExistInDb(user.getLogin())).thenReturn(false);
    when(userValidator.isEmailExistInDb(user.getEmail())).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);

    final var result = userService.createUser(user);

    assertSame(user, result);
    verify(userValidator, times(1)).isLoginExistInDb(user.getLogin());
    verify(userValidator, times(1)).isEmailExistInDb(user.getEmail());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void testCreateUser_WhenLoginExists_ShouldThrowException() {
    final var user = new User();
    when(userValidator.isLoginExistInDb(user.getLogin())).thenReturn(true);

    assertThrows(EntityExistsException.class, () -> userService.createUser(user));
    verify(userValidator, times(1)).isLoginExistInDb(user.getLogin());
    verify(userValidator, never()).isEmailExistInDb(user.getEmail());
    verify(userRepository, never()).save(user);
  }

  @Test
  void testCreateUser_WhenEmailExists_ShouldThrowException() {
    final var user = new User();
    when(userValidator.isLoginExistInDb(user.getLogin())).thenReturn(false);
    when(userValidator.isEmailExistInDb(user.getEmail())).thenReturn(true);

    assertThrows(EntityExistsException.class, () -> userService.createUser(user));
    verify(userValidator, times(1)).isLoginExistInDb(user.getLogin());
    verify(userValidator, times(1)).isEmailExistInDb(user.getEmail());
    verify(userRepository, never()).save(user);
  }

  @Test
  void testUpdateUser_WhenUserExists_ShouldUpdateUser() {
    final var userDto = new UserDto();
    userDto.setLogin(EXISTING_LOGIN);

    final var existingUser = new User();
    when(userValidator.isLoginExistInDb(userDto.getLogin())).thenReturn(true);
    when(userRepository.findUserByLoginOrEmail(EXISTING_LOGIN, null))
        .thenReturn(Optional.of(existingUser));
    when(userRepository.save(existingUser)).thenReturn(existingUser);

    final var result = userService.updateUser(userDto);

    assertSame(existingUser, result);
    verify(userValidator, times(1)).isLoginExistInDb(userDto.getLogin());
    verify(userMapper, times(1)).updateUser(existingUser, userDto);
    verify(userRepository, times(1)).save(existingUser);
  }

  @Test
  void testUpdateUser_WhenUserDoesNotExist_ShouldThrowException() {
    final var userDto = new UserDto();
    userDto.setLogin(NON_EXISTING_LOGIN);

    when(userValidator.isLoginExistInDb(userDto.getLogin())).thenReturn(false);

    assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDto));
    verify(userValidator, times(1)).isLoginExistInDb(userDto.getLogin());
    verify(userMapper, never()).updateUser(any(), any());
    verify(userRepository, never()).save(any());
  }

  @Test
  void testDeleteUserByLoginOrEmail_WhenUserExists_ShouldDeleteUser() {
    when(userValidator.isLoginExistInDb(EXISTING_LOGIN)).thenReturn(true);

    userService.deleteUserByLoginOrEmail(EXISTING_LOGIN, EXISTING_EMAIL);

    verify(userValidator, times(1)).isLoginExistInDb(EXISTING_LOGIN);
    verify(userRepository, times(1)).deleteUserByLoginOrEmail(EXISTING_LOGIN, EXISTING_EMAIL);
  }

  @Test
  void testDeleteUserByLoginOrEmail_WhenUserDoesNotExist_ShouldThrowException() {
    when(userValidator.isLoginExistInDb(NON_EXISTING_LOGIN)).thenReturn(false);
    when(userValidator.isEmailExistInDb(NON_EXISTING_EMAIL)).thenReturn(true);

    assertThrows(
        UserNotFoundException.class,
        () -> userService.deleteUserByLoginOrEmail(NON_EXISTING_LOGIN, NON_EXISTING_EMAIL));
    verify(userValidator, times(1)).isLoginExistInDb(NON_EXISTING_LOGIN);
    verify(userValidator, times(1)).isEmailExistInDb(NON_EXISTING_EMAIL);
    verify(userRepository, never())
        .deleteUserByLoginOrEmail(NON_EXISTING_LOGIN, NON_EXISTING_EMAIL);
  }
}
