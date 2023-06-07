package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.illiapinchuk.moodle.common.validator.EmailValidator;
import com.illiapinchuk.moodle.configuration.security.jwt.JwtTokenProviderImpl;
import com.illiapinchuk.moodle.model.dto.AuthRequestDto;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.service.UserService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
  private static final String VALID_LOGIN_OR_EMAIL = "testuser";
  private static final String INVALID_EMAIL = "invalidemail";
  private static final String VALID_PASSWORD = "password";
  private static final String INVALID_PASSWORD = "invalidpassword";
  private static final String TOKEN = "example-token";

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtTokenProviderImpl jwtTokenProvider;

  @Mock
  private UserService userService;

  @Mock
  private EmailValidator emailValidator;

  @InjectMocks
  private AuthenticationServiceImpl authenticationService;

  @Test
  void testLogin_WhenValidCredentials_ShouldReturnToken() {
    final var requestDto = AuthRequestDto.builder()
        .loginOrEmail(VALID_LOGIN_OR_EMAIL)
        .password(VALID_PASSWORD)
        .build();

    final var user = new User();
    user.setRoles(Set.of(RoleName.USER));

    when(emailValidator.isValid(VALID_LOGIN_OR_EMAIL, null)).thenReturn(false);
    when(userService.getUserByLoginOrEmail(VALID_LOGIN_OR_EMAIL, null)).thenReturn(user);
    when(jwtTokenProvider.createToken(VALID_LOGIN_OR_EMAIL, user.getRoles())).thenReturn(TOKEN);

    final var result = authenticationService.login(requestDto);

    assertNotNull(result);
    assertEquals(VALID_LOGIN_OR_EMAIL, result.get("loginOrEmail"));
    assertEquals(TOKEN, result.get("token"));
    verify(authenticationManager).authenticate(
        new UsernamePasswordAuthenticationToken(VALID_LOGIN_OR_EMAIL, VALID_PASSWORD));
    verify(emailValidator).isValid(VALID_LOGIN_OR_EMAIL, null);
    verify(userService).getUserByLoginOrEmail(VALID_LOGIN_OR_EMAIL, null);
    verify(jwtTokenProvider).createToken(VALID_LOGIN_OR_EMAIL, user.getRoles());
  }

  @Test
  void testLogin_WhenInvalidCredentials_ShouldThrowBadCredentialsException() {
    final var requestDto = AuthRequestDto.builder()
        .loginOrEmail(VALID_LOGIN_OR_EMAIL)
        .password(INVALID_PASSWORD)
        .build();

    when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

    assertThrows(BadCredentialsException.class, () -> authenticationService.login(requestDto));

    verify(authenticationManager).authenticate(
        new UsernamePasswordAuthenticationToken(VALID_LOGIN_OR_EMAIL, INVALID_PASSWORD));
  }

  @Test
  void testLogin_WhenValidCredentialsAndNoRoles_ShouldReturnTokenWithEmptyRoles() {
    final var requestDto = AuthRequestDto.builder()
        .loginOrEmail(VALID_LOGIN_OR_EMAIL)
        .password(VALID_PASSWORD)
        .build();

    final var user = new User();
    user.setRoles(Set.of()); // Empty set of roles

    when(emailValidator.isValid(VALID_LOGIN_OR_EMAIL, null)).thenReturn(false);
    when(userService.getUserByLoginOrEmail(VALID_LOGIN_OR_EMAIL, null)).thenReturn(user);
    when(jwtTokenProvider.createToken(VALID_LOGIN_OR_EMAIL, user.getRoles())).thenReturn(TOKEN);

    final var result = authenticationService.login(requestDto);

    assertNotNull(result);
    assertEquals(VALID_LOGIN_OR_EMAIL, result.get("loginOrEmail"));
    assertEquals(TOKEN, result.get("token"));
    verify(authenticationManager).authenticate(
        new UsernamePasswordAuthenticationToken(VALID_LOGIN_OR_EMAIL, VALID_PASSWORD));
    verify(emailValidator).isValid(VALID_LOGIN_OR_EMAIL, null);
    verify(userService).getUserByLoginOrEmail(VALID_LOGIN_OR_EMAIL, null);
    verify(jwtTokenProvider).createToken(VALID_LOGIN_OR_EMAIL, user.getRoles());
  }

  @Test
  void testLogin_WhenInvalidCredentialsPassword_ShouldThrowBadCredentialsException() {
    final var requestDto = AuthRequestDto.builder()
        .loginOrEmail(VALID_LOGIN_OR_EMAIL)
        .password(INVALID_PASSWORD)
        .build();

    when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

    assertThrows(BadCredentialsException.class, () -> authenticationService.login(requestDto));

    verify(authenticationManager).authenticate(
        new UsernamePasswordAuthenticationToken(VALID_LOGIN_OR_EMAIL, INVALID_PASSWORD));
  }

  @Test
  void testLogin_WhenInvalidCredentialsUsernameOrEmail_ShouldThrowBadCredentialsException() {
    final var requestDto = AuthRequestDto.builder()
        .loginOrEmail(INVALID_EMAIL)
        .password(INVALID_PASSWORD)
        .build();

    when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

    assertThrows(BadCredentialsException.class, () -> authenticationService.login(requestDto));

    verify(authenticationManager).authenticate(
        new UsernamePasswordAuthenticationToken(INVALID_EMAIL, INVALID_PASSWORD));
  }

}
