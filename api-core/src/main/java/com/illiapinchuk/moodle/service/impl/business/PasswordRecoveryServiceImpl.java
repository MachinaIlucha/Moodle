package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import com.illiapinchuk.moodle.model.dto.EmailDto;
import com.illiapinchuk.moodle.model.entity.UserTokenStatus;
import com.illiapinchuk.moodle.persistence.entity.UserToken;
import com.illiapinchuk.moodle.service.business.EmailService;
import com.illiapinchuk.moodle.service.business.PasswordRecoveryService;
import com.illiapinchuk.moodle.service.business.UserService;
import com.illiapinchuk.moodle.service.business.UserTokenService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the {@link PasswordRecoveryService} interface that provides methods for sending
 * password recovery emails with a token.
 */
@Service
@RequiredArgsConstructor
@Slf4j
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

    final var userToken =
        UserToken.builder()
            .userId(user.getId())
            .token(token)
            .userTokenStatus(UserTokenStatus.WAITING)
            .build();
    userTokenService.saveToken(userToken);

    log.info("User token was saved to the database: {}", userToken.getToken());

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
