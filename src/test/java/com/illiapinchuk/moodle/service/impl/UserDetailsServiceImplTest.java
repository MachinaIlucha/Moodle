package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.illiapinchuk.moodle.common.validator.EmailValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  private static final String VALID_EMAIL = "test@example.com";
  private static final String VALID_USERNAME = "testuser";
  private static final String NON_EXISTENT_USERNAME = "nonexistentuser";

  @Mock
  private UserRepository userRepository;

  @Mock
  private EmailValidator emailValidator;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Test
  void testLoadUserByUsername_WhenUserExistsByEmail_ShouldReturnUserDetails() {
    final var user = new User();
    user.setRoles(Set.of(RoleName.USER)); // Set a non-null Set of roles
    when(emailValidator.isValid(VALID_EMAIL, null)).thenReturn(true);
    when(userRepository.findUserByLoginOrEmail(null, VALID_EMAIL)).thenReturn(Optional.of(user));

    final var userDetails = userDetailsService.loadUserByUsername(VALID_EMAIL);

    assertNotNull(userDetails);
    verify(emailValidator).isValid(VALID_EMAIL, null);
    verify(userRepository).findUserByLoginOrEmail(null, VALID_EMAIL);
  }

  @Test
  void testLoadUserByUsername_WhenUserExistsByUsername_ShouldReturnUserDetails() {
    final var user = new User();
    user.setRoles(Set.of(RoleName.USER)); // Set a non-null Set of roles
    when(emailValidator.isValid(VALID_USERNAME, null)).thenReturn(false);
    when(userRepository.findUserByLoginOrEmail(VALID_USERNAME, null)).thenReturn(Optional.of(user));

    final var userDetails = userDetailsService.loadUserByUsername(VALID_USERNAME);

    assertNotNull(userDetails);
    verify(emailValidator).isValid(VALID_USERNAME, null);
    verify(userRepository).findUserByLoginOrEmail(VALID_USERNAME, null);
  }

  @Test
  void testLoadUserByUsername_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {
    when(emailValidator.isValid(NON_EXISTENT_USERNAME, null)).thenReturn(false);
    when(userRepository.findUserByLoginOrEmail(NON_EXISTENT_USERNAME, null)).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class, () -> userDetailsService.loadUserByUsername(NON_EXISTENT_USERNAME));

    verify(emailValidator).isValid(NON_EXISTENT_USERNAME, null);
    verify(userRepository).findUserByLoginOrEmail(NON_EXISTENT_USERNAME, null);
  }
}
