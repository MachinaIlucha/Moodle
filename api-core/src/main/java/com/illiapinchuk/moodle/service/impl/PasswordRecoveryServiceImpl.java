package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import com.illiapinchuk.moodle.model.dto.EmailDto;
import com.illiapinchuk.moodle.persistence.entity.UserToken;
import com.illiapinchuk.moodle.service.EmailService;
import com.illiapinchuk.moodle.service.PasswordRecoveryService;
import com.illiapinchuk.moodle.service.UserService;
import com.illiapinchuk.moodle.service.UserTokenService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the {@link PasswordRecoveryService} interface that provides methods for sending
 * password recovery emails with a token.
 */
@Service
@RequiredArgsConstructor
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

  private final UserService userService;
  private final UserTokenService userTokenService;
  private final EmailService emailService;

  /**
   * Sends a password recovery email to the specified user with a unique token.
   *
   * @param userEmail the email address of the user requesting password recovery
   * @param scheme the scheme of the URL (e.g., "http" or "https")
   * @param serverName the server name or IP address
   * @param serverPort the port number
   */
  @Transactional
  @Override
  public void sendPasswordRecoveryEmailWithToken(
      @Nonnull final String userEmail,
      @Nonnull final String scheme,
      @Nonnull final String serverName,
      @Nonnull final String serverPort) {
    // Lookup user in database by email
    final var user = userService.getUserByLoginOrEmail(null, userEmail);

    final var token = UUID.randomUUID().toString();

    final var userToken = UserToken.builder().userId(user.getId()).token(token).build();
    userTokenService.saveToken(userToken);

    final var resetUrlWithToken =
        scheme
            + "://"
            + serverName
            + ":"
            + serverPort
            + ApplicationConstants.Web.PasswordRecovery.RESET_PASSWORD_LINK
            + userToken.getToken();

    // Construct the email message
    final var message =
        String.format(
            ApplicationConstants.Web.PasswordRecovery.RESET_PASSWORD_LETTER, resetUrlWithToken);

    final var emailDto =
        EmailDto.builder()
            .to(user.getEmail())
            .subject(ApplicationConstants.Web.PasswordRecovery.LETTER_SUBJECT)
            .content(message)
            .build();

    emailService.sendEmail(emailDto);
  }
}
