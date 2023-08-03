package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.common.TestConstants;
import com.illiapinchuk.moodle.model.dto.EmailDto;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.service.EmailService;
import com.illiapinchuk.moodle.service.UserService;
import com.illiapinchuk.moodle.service.UserTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceImplTest {

  @Mock private UserService userService;

  @Mock private UserTokenService userTokenService;

  @Mock private EmailService emailService;

  @Captor private ArgumentCaptor<EmailDto> emailDtoCaptor;

  @InjectMocks private PasswordRecoveryServiceImpl passwordRecoveryService;

  @Test
  void testSendPasswordRecoveryEmailWithToken_nullEmail() {
    assertThrows(
        NullPointerException.class,
        () -> {
          passwordRecoveryService.sendPasswordRecoveryEmailWithToken(
              null, "https", "moodle.com", "8080");
        });
  }

  @Test
  void testSendPasswordRecoveryEmailWithToken_invalidUser() {
    when(userService.getUserByLoginOrEmail(null, TestConstants.UserConstants.USER_INVALID_EMAIL))
        .thenReturn(null);

    assertThrows(
        NullPointerException.class,
        () -> {
          passwordRecoveryService.sendPasswordRecoveryEmailWithToken(
              TestConstants.UserConstants.USER_INVALID_EMAIL, "https", "moodle.com", "8080");
        });
  }

  @Test
  void testSendPasswordRecoveryEmailWithToken_validUser() {
    final User user = mock(User.class);
    when(user.getId()).thenReturn(Long.valueOf(TestConstants.UserConstants.USER_ID));
    when(user.getEmail()).thenReturn(TestConstants.UserConstants.USER_EMAIL);
    when(userService.getUserByLoginOrEmail(null, TestConstants.UserConstants.USER_EMAIL))
        .thenReturn(user);

    passwordRecoveryService.sendPasswordRecoveryEmailWithToken(
        TestConstants.UserConstants.USER_EMAIL, "https", "test.com", "8080");

    verify(emailService).sendEmail(emailDtoCaptor.capture());
    assertEquals(TestConstants.UserConstants.USER_EMAIL, emailDtoCaptor.getValue().getTo());
  }

    @Test
    void testSendPasswordRecoveryEmailWithToken_emailNotSent() {
      final User user = mock(User.class);
      when(user.getId()).thenReturn(Long.valueOf(TestConstants.UserConstants.USER_ID));
      when(user.getEmail()).thenReturn(TestConstants.UserConstants.USER_EMAIL);
      when(userService.getUserByLoginOrEmail(null, TestConstants.UserConstants.USER_EMAIL)).thenReturn(user);

      doThrow(new RuntimeException()).when(emailService).sendEmail(any(EmailDto.class));

      assertThrows(
          RuntimeException.class,
          () -> {
            passwordRecoveryService.sendPasswordRecoveryEmailWithToken(
                    TestConstants.UserConstants.USER_EMAIL, "https", "moodle.com", "8080");
          });
    }
}
