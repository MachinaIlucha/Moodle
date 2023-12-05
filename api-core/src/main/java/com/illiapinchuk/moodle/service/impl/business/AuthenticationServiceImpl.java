package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.validator.EmailValidator;
import com.illiapinchuk.moodle.configuration.security.jwt.JwtTokenProviderImpl;
import com.illiapinchuk.moodle.model.dto.AuthRequestDto;
import com.illiapinchuk.moodle.service.business.AuthenticationService;
import com.illiapinchuk.moodle.service.business.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/** Implementation of {@link AuthenticationService} interface. */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProviderImpl jwtTokenProviderImpl;
  private final UserService userService;
  private final EmailValidator emailValidator;

  @Override
  public Map<Object, Object> login(AuthRequestDto requestDto) {
    final var loginOrEmail = requestDto.getLoginOrEmail();
    final var password = requestDto.getPassword();

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginOrEmail, password));

      final var user =
          emailValidator.isValid(loginOrEmail, null)
              ? userService.getUserByLoginOrEmail(null, loginOrEmail)
              : userService.getUserByLoginOrEmail(loginOrEmail, null);

      final var token = jwtTokenProviderImpl.createToken(loginOrEmail, user.getRoles());

      return Map.of("loginOrEmail", loginOrEmail, "token", token);
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }
}
