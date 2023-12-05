package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.exception.UserTokenNotFoundException;
import com.illiapinchuk.moodle.model.entity.UserTokenStatus;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.entity.UserToken;
import com.illiapinchuk.moodle.persistence.repository.UserTokenRepository;
import com.illiapinchuk.moodle.service.business.UserService;
import com.illiapinchuk.moodle.service.business.UserTokenService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of the UserTokenService interface. */
@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

  private final UserTokenRepository userTokenRepository;
  private final UserService userService;

  /**
   * Saves a UserToken in the database.
   *
   * @param userToken the UserToken to save
   */
  @Override
  public void saveToken(@Nonnull final UserToken userToken) {
    userTokenRepository.save(userToken);
  }

  /**
   * Deletes a UserToken by the specified user ID.
   *
   * @param userId the ID of the user associated with the token
   */
  @Transactional
  @Override
  public void deleteTokenByUserId(@Nonnull final Long userId) {
    userTokenRepository.deleteUserTokenByUserId(userId);
  }

  /**
   * Retrieves a User by the specified token.
   *
   * @param token the token string
   * @return the User associated with the token
   * @throws UserNotFoundException if no user is found for the given token
   */
  @Override
  public User getUserByToken(@Nonnull final String token) {
    final var userToken =
        userTokenRepository
            .getUserTokenByToken(token)
            .orElseThrow(() -> new UserNotFoundException("No user was found for this token."));
    final var userId = userToken.getUserId();

    return userService.getUserById(userId);
  }

  @Override
  @Transactional
  public void setTokenUsed(@Nonnull final String token) {
    if (userTokenRepository.getUserTokenByToken(token).isEmpty()) {
      throw new UserTokenNotFoundException("No user token was found for this token.");
    }

    userTokenRepository.updateUserTokenStatusByToken(token, UserTokenStatus.USED);
  }

  @Override
  @Transactional
  @Scheduled(cron = "0 0 * * * *")
  public void deleteUsedTokens() {
    userTokenRepository.deleteUserTokensByUserTokenStatus(UserTokenStatus.USED);
  }
}
