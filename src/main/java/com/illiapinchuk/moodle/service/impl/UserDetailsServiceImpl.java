package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.validator.EmailValidator;
import com.illiapinchuk.moodle.configuration.security.jwt.JwtUser;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Implementation of {@link UserDetailsService} interface. */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private final EmailValidator emailValidator;

  /**
   * Loads user details by username or email.
   *
   * @param loginOrEmail the username or email of the user to load
   * @return the UserDetails object for the user
   * @throws UsernameNotFoundException if the user with the specified username or email is not found
   */
  @Override
  public UserDetails loadUserByUsername(@Nonnull final String loginOrEmail)
      throws UsernameNotFoundException {
    return JwtUser.build(
        emailValidator.isValid(loginOrEmail, null)
            ? userRepository
                .findUserByLoginOrEmail(null, loginOrEmail)
                .orElseThrow(() -> new UserNotFoundException("User with current email not found"))
            : userRepository
                .findUserByLoginOrEmail(loginOrEmail, null)
                .orElseThrow(() -> new UserNotFoundException("User with current login not found")));
  }
}
