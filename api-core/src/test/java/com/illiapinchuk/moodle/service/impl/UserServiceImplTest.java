package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.mapper.UserMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
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

  private static MockedStatic<UserPermissionService> mockedUserPermissionService;

  @BeforeAll
  static void setupUserPermissionServiceMocks() {
    mockedUserPermissionService = mockStatic(UserPermissionService.class);
    mockedUserPermissionService
        .when(UserPermissionService::getJwtUser)
        .thenReturn(TestConstants.UserConstants.ADMIN_JWT_USER);
    mockedUserPermissionService.when(UserPermissionService::hasAnyRulingRole).thenReturn(true);
  }

  @AfterAll
  static void closeUserPermissionServiceMocks() {
    mockedUserPermissionService.close();
  }

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
    when(userRepository.getReferenceById(TestConstants.UserConstants.VALID_USER_DTO.getId()))
        .thenReturn(TestConstants.UserConstants.VALID_USER);
    when(userRepository.save(TestConstants.UserConstants.VALID_USER))
        .thenReturn(TestConstants.UserConstants.VALID_USER);
    when(userValidator.isUserExistInDbById(TestConstants.UserConstants.VALID_USER_DTO.getId()))
        .thenReturn(true);

    final var result = userService.updateUser(TestConstants.UserConstants.VALID_USER_DTO);

    assertEquals(TestConstants.UserConstants.VALID_USER, result);
    verify(userMapper)
        .updateUser(
            TestConstants.UserConstants.VALID_USER, TestConstants.UserConstants.VALID_USER_DTO);
    verify(userRepository).save(TestConstants.UserConstants.VALID_USER);
  }

  @Test
  void updateUser_UserWithNonExistingLogin_ThrowsUserNotFoundException() {
    assertThrows(
        UserNotFoundException.class,
        () -> userService.updateUser(TestConstants.UserConstants.VALID_USER_DTO));
    verify(userRepository, never()).findUserByLoginOrEmail(anyString(), anyString());
    verify(userMapper, never()).updateUser(any(User.class), any(UserDto.class));
    verify(userRepository, never()).save(any(User.class));
  }

  //  @Test
  //  void updateUser_NonAdminUpdatingAnotherUser_ThrowsException() {
  //    when(userValidator.isUserExistInDbById(any())).thenReturn(true);
  //
  //    assertThrows(
  //        UserCantModifyAnotherUserException.class,
  //        () -> userService.updateUser(TestConstants.UserConstants.VALID_USER_DTO));
  //  }

  //  @Test
  //  void updateUser_NonAdminWithoutUserRoleUpdatingSelf_ThrowsException() {
  //    when(userValidator.isUserExistInDbById(any())).thenReturn(true);
  //
  //    assertThrows(
  //        UserCantModifyAnotherUserException.class,
  //        () -> userService.updateUser(TestConstants.UserConstants.VALID_USER_DTO));
  //  }

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
