package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.common.TestConstants;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @Mock private UserValidator userValidator;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private UserServiceImpl userService;

  @Test
  void getUserByLoginOrEmail_UserExists_ReturnsUser() {
    when(userRepository.findUserByLoginOrEmail(TestConstants.UserConstants.USER_LOGIN, null))
        .thenReturn(Optional.of(TestConstants.UserConstants.VALID_USER));

    final var result =
        userService.getUserByLoginOrEmail(TestConstants.UserConstants.USER_LOGIN, null);

    assertEquals(TestConstants.UserConstants.VALID_USER, result);
  }

  @Test
  void getUserByLoginOrEmail_UserDoesNotExist_ThrowsUserNotFoundException() {
    when(userRepository.findUserByLoginOrEmail(
            null, TestConstants.UserConstants.USER_INVALID_EMAIL))
        .thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () ->
            userService.getUserByLoginOrEmail(
                null, TestConstants.UserConstants.USER_INVALID_EMAIL));
  }

  @Test
  void createUser_ValidUser_CreatesAndReturnsUser() {
    when(userValidator.isLoginExistInDb(TestConstants.UserConstants.VALID_USER.getLogin()))
        .thenReturn(false);
    when(userValidator.isEmailExistInDb(TestConstants.UserConstants.VALID_USER.getEmail()))
        .thenReturn(false);
    when(userRepository.save(TestConstants.UserConstants.VALID_USER))
        .thenReturn(TestConstants.UserConstants.VALID_USER);
    when(passwordEncoder.encode(TestConstants.UserConstants.VALID_USER.getPassword()))
        .thenReturn(TestConstants.UserConstants.VALID_USER.getPassword());

    final var result = userService.createUser(TestConstants.UserConstants.VALID_USER);

    assertEquals(TestConstants.UserConstants.VALID_USER, result);
    verify(passwordEncoder).encode(TestConstants.UserConstants.VALID_USER.getPassword());
  }

  @Test
  void createUser_UserWithExistingLogin_ThrowsEntityExistsException() {
    when(userValidator.isLoginExistInDb(TestConstants.UserConstants.VALID_USER.getLogin()))
        .thenReturn(true);

    assertThrows(
        EntityExistsException.class,
        () -> userService.createUser(TestConstants.UserConstants.VALID_USER));
    verify(userRepository, never()).save(TestConstants.UserConstants.VALID_USER);
  }

  @Test
  void createUser_UserWithExistingEmail_ThrowsEntityExistsException() {
    when(userValidator.isLoginExistInDb(TestConstants.UserConstants.VALID_USER.getLogin()))
        .thenReturn(false);
    when(userValidator.isEmailExistInDb(TestConstants.UserConstants.VALID_USER.getEmail()))
        .thenReturn(true);

    assertThrows(
        EntityExistsException.class,
        () -> userService.createUser(TestConstants.UserConstants.VALID_USER));
    verify(userRepository, never()).save(TestConstants.UserConstants.VALID_USER);
  }

  @Test
  void updateUser_ValidUserDto_UpdatesAndReturnsUser() {
    when(userValidator.isLoginExistInDb(TestConstants.UserConstants.VALID_USER_DTO.getLogin()))
        .thenReturn(true);
    when(userRepository.findUserByLoginOrEmail(
            TestConstants.UserConstants.VALID_USER_DTO.getLogin(), null))
        .thenReturn(Optional.of(TestConstants.UserConstants.VALID_USER));
    when(userRepository.save(TestConstants.UserConstants.VALID_USER))
        .thenReturn(TestConstants.UserConstants.VALID_USER);

    final var result = userService.updateUser(TestConstants.UserConstants.VALID_USER_DTO);

    assertEquals(TestConstants.UserConstants.VALID_USER, result);
    verify(userMapper)
        .updateUser(
            TestConstants.UserConstants.VALID_USER, TestConstants.UserConstants.VALID_USER_DTO);
    verify(userRepository).save(TestConstants.UserConstants.VALID_USER);
  }

  @Test
  void updateUser_UserWithNonExistingLogin_ThrowsUserNotFoundException() {
    when(userValidator.isLoginExistInDb(TestConstants.UserConstants.VALID_USER_DTO.getLogin()))
        .thenReturn(false);

    assertThrows(
        UserNotFoundException.class,
        () -> userService.updateUser(TestConstants.UserConstants.VALID_USER_DTO));
    verify(userRepository, never()).findUserByLoginOrEmail(anyString(), anyString());
    verify(userMapper, never()).updateUser(any(User.class), any(UserDto.class));
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void deleteUserByLoginOrEmail_UserWithExistingLogin_DeletesUser() {
    when(userValidator.isLoginExistInDb(TestConstants.UserConstants.USER_LOGIN)).thenReturn(true);

    userService.deleteUserByLoginOrEmail(TestConstants.UserConstants.USER_LOGIN, null);

    verify(userRepository).deleteUserByLoginOrEmail(TestConstants.UserConstants.USER_LOGIN, null);
  }

  @Test
  void
      deleteUserByLoginOrEmail_UserWithNonExistingLoginAndExistingEmail_ThrowsUserNotFoundException() {
    when(userValidator.isLoginExistInDb(TestConstants.UserConstants.USER_LOGIN)).thenReturn(false);
    when(userValidator.isEmailExistInDb(TestConstants.UserConstants.USER_EMAIL)).thenReturn(true);

    assertThrows(
        UserNotFoundException.class,
        () ->
            userService.deleteUserByLoginOrEmail(
                TestConstants.UserConstants.USER_LOGIN, TestConstants.UserConstants.USER_EMAIL));
    verify(userRepository, never()).deleteUserByLoginOrEmail(anyString(), anyString());
  }
}
