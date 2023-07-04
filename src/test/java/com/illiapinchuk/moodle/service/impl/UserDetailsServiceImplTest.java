package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.validator.EmailValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private EmailValidator emailValidator;
  @InjectMocks private UserDetailsServiceImpl userDetailsService;

  @Test
  void loadUserByUsername_WithValidEmail_ShouldReturnUserDetails() {
    when(emailValidator.isValid(TestConstants.UserConstants.USER_EMAIL, null)).thenReturn(true);
    when(userRepository.findUserByLoginOrEmail(null, TestConstants.UserConstants.USER_EMAIL))
        .thenReturn(Optional.of(TestConstants.UserConstants.VALID_USER));

    final var userDetails =
        userDetailsService.loadUserByUsername(TestConstants.UserConstants.USER_EMAIL);

    assertNotNull(userDetails);
  }

  @Test
  void loadUserByUsername_WithInvalidEmail_ShouldThrowUserNotFoundException() {
    when(emailValidator.isValid(TestConstants.UserConstants.USER_EMAIL, null)).thenReturn(true);
    when(userRepository.findUserByLoginOrEmail(null, TestConstants.UserConstants.USER_EMAIL))
        .thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> {
          userDetailsService.loadUserByUsername(TestConstants.UserConstants.USER_EMAIL);
        });
  }

  @Test
  void loadUserByUsername_WithValidUsername_ShouldReturnUserDetails() {
    when(emailValidator.isValid(TestConstants.UserConstants.USER_LOGIN, null)).thenReturn(false);
    when(userRepository.findUserByLoginOrEmail(TestConstants.UserConstants.USER_LOGIN, null))
        .thenReturn(Optional.of(TestConstants.UserConstants.VALID_USER));

    final var userDetails =
        userDetailsService.loadUserByUsername(TestConstants.UserConstants.USER_LOGIN);

    assertNotNull(userDetails);
  }

  @Test
  void loadUserByUsername_WithInvalidUsername_ShouldThrowUserNotFoundException() {
    when(emailValidator.isValid(TestConstants.UserConstants.USER_LOGIN, null)).thenReturn(false);
    when(userRepository.findUserByLoginOrEmail(TestConstants.UserConstants.USER_LOGIN, null))
        .thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> {
          userDetailsService.loadUserByUsername(TestConstants.UserConstants.USER_LOGIN);
        });
  }
}
