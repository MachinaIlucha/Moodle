package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.common.mapper.UserMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.service.PasswordRecoveryService;
import com.illiapinchuk.moodle.service.UserService;
import com.illiapinchuk.moodle.service.UserTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * The PasswordController class handles REST API requests related to password management. It
 * provides endpoints for password recovery and password reset.
 */
@RestController
@RequestMapping(value = "/password")
@RequiredArgsConstructor
@Slf4j
public class PasswordController {

  private final PasswordRecoveryService passwordRecoveryService;
  private final UserValidator userValidator;
  private final UserTokenService userTokenService;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  /**
   * Initiates the password recovery process by sending a recovery email to the user's email
   * address.
   *
   * @param userEmail the email address of the user requesting password recovery
   * @param request the HTTP servlet request
   * @return a ResponseEntity with no content
   * @throws UserNotFoundException if the user email is not found in the database
   */
  @PostMapping(value = "/forgot")
  public ResponseEntity<Void> processForgotPasswordForm(
      @RequestParam("email") @NotBlank final String userEmail, final HttpServletRequest request) {
    log.info("Got a request to initiate recovery password for email: {}", userEmail);

    final String scheme = request.getScheme(); // Capture the scheme
    final String serverName = request.getServerName(); // Capture the server name
    final String serverPort = String.valueOf(request.getServerPort());

    CompletableFuture.runAsync(
        () -> {
          if (userValidator.isEmailExistInDb(userEmail)) {
            passwordRecoveryService.sendPasswordRecoveryEmailWithToken(
                userEmail, scheme, serverName, serverPort);
            log.info("An email with a recovery link was sent to email: {}", userEmail);
          } else {
            log.error("Email was not found in db: {}", userEmail);
            throw new UserNotFoundException(
                String.format(
                    "The user with the current email %s is not found in the database.", userEmail));
          }
        });

    return ResponseEntity.noContent().build();
  }

  /**
   * Resets the user's password using a password reset token.
   *
   * @param token the password reset token
   * @param password the new password to be set
   * @return a ResponseEntity with no content
   */
  @PostMapping(value = "/reset")
  public ResponseEntity<Void> setNewPassword(
      @RequestParam("token") final String token, @RequestParam("password") final String password) {
    final var encodedPassword = passwordEncoder.encode(password);

    // Find the user associated with the reset token
    final var user = userTokenService.getUserByToken(token);
    final var userDto = userMapper.userToUserDto(user);
    userDto.setPassword(encodedPassword);

    // Delete token, so now no-one can use it one more time
    userTokenService.deleteTokenByUserId(user.getId());

    // Update user
    userService.updateUser(userDto);

    return ResponseEntity.ok().build();
  }
}
