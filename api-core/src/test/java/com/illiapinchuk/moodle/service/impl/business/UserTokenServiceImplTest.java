package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.exception.UserTokenNotFoundException;
import com.illiapinchuk.moodle.model.entity.UserTokenStatus;
import com.illiapinchuk.moodle.persistence.entity.UserToken;
import com.illiapinchuk.moodle.persistence.repository.UserTokenRepository;
import com.illiapinchuk.moodle.service.business.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTokenServiceImplTest {

  @InjectMocks private UserTokenServiceImpl userTokenService;

  @Mock private UserTokenRepository userTokenRepository;

  @Mock private UserService userService;

  @Test
  void setTokenUsed_WhenTokenNotFound_ShouldThrowException() {
    String token = "nonexistent-token";
    when(userTokenRepository.getUserTokenByToken(token)).thenReturn(Optional.empty());

    assertThrows(
        UserTokenNotFoundException.class,
        () -> {
          userTokenService.setTokenUsed(token);
        });
  }

  @Test
  void setTokenUsed_WhenTokenFound_ShouldUpdateTokenStatus() {
    String token = "existing-token";
    when(userTokenRepository.getUserTokenByToken(token)).thenReturn(Optional.of(new UserToken()));

    userTokenService.setTokenUsed(token);

    verify(userTokenRepository, times(1)).updateUserTokenStatusByToken(token, UserTokenStatus.USED);
  }

  @Test
  void deleteUsedTokens_ShouldInvokeRepositoryMethod() {
    userTokenService.deleteUsedTokens();

    verify(userTokenRepository, times(1)).deleteUserTokensByUserTokenStatus(UserTokenStatus.USED);
  }

  @Test
  void saveTokenShouldSaveToken() {
    userTokenService.saveToken(TestConstants.UserTokenConstants.VALID_USER_TOKEN);

    verify(userTokenRepository, times(1)).save(any(UserToken.class));
  }

  @Test
  void deleteTokenByUserIdShouldDeleteToken() {
    doNothing().when(userTokenRepository).deleteUserTokenByUserId(any(Long.class));

    userTokenService.deleteTokenByUserId(Long.valueOf(TestConstants.UserConstants.USER_ID));

    verify(userTokenRepository, times(1)).deleteUserTokenByUserId(any(Long.class));
  }

  @Test
  void getUserByTokenShouldReturnUser() {
    when(userTokenRepository.getUserTokenByToken(any(String.class)))
        .thenReturn(Optional.of(TestConstants.UserTokenConstants.VALID_USER_TOKEN));
    when(userService.getUserById(any(Long.class)))
        .thenReturn(TestConstants.UserConstants.VALID_USER);

    final var returnedUser =
        userTokenService.getUserByToken(TestConstants.UserTokenConstants.VALID_TOKEN);

    verify(userTokenRepository, times(1)).getUserTokenByToken(any(String.class));
    verify(userService, times(1)).getUserById(any(Long.class));
    assertEquals(returnedUser.getId(), TestConstants.UserConstants.VALID_USER.getId());
  }

  @Test
  void getUserByTokenShouldThrowUserNotFoundException() {
    when(userTokenRepository.getUserTokenByToken(any(String.class))).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> {
          userTokenService.getUserByToken(TestConstants.UserTokenConstants.VALID_TOKEN);
        });
  }
}
