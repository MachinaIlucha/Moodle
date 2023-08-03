package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.validator.EmailValidator;
import com.illiapinchuk.moodle.configuration.security.jwt.JwtTokenProviderImpl;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

  @Mock private AuthenticationManager authenticationManager;

  @Mock private JwtTokenProviderImpl jwtTokenProviderImpl;

  @Mock private UserService userService;

  @Mock private EmailValidator emailValidator;

  @InjectMocks private AuthenticationServiceImpl authenticationService;

  @Test
  void login_ValidCredentialsWithEmail_ReturnsToken() {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(null);

    when(emailValidator.isValid(
            TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail(), null))
        .thenReturn(true);
    when(userService.getUserByLoginOrEmail(
            null, TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail()))
        .thenReturn(new User());

    when(jwtTokenProviderImpl.createToken(
            eq(TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail()), any()))
        .thenReturn("token");

    final var result = authenticationService.login(TestConstants.AuthConstants.AUTH_REQUEST_DTO);

    assertNotNull(result);
    assertEquals(
        TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail(), result.get("loginOrEmail"));
    assertEquals("token", result.get("token"));

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(emailValidator)
        .isValid(TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail(), null);
    verify(userService)
        .getUserByLoginOrEmail(
            null, TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail());
    verify(jwtTokenProviderImpl)
        .createToken(eq(TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail()), any());
  }

  @Test
  void login_ValidCredentialsWithUsername_ReturnsToken() {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(null);

    when(emailValidator.isValid(
            TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail(), null))
        .thenReturn(false);
    when(userService.getUserByLoginOrEmail(
            TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail(), null))
        .thenReturn(new User());

    when(jwtTokenProviderImpl.createToken(
            eq(TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail()), any()))
        .thenReturn("token");

    final var result = authenticationService.login(TestConstants.AuthConstants.AUTH_REQUEST_DTO);

    assertNotNull(result);
    assertEquals(
        TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail(), result.get("loginOrEmail"));
    assertEquals("token", result.get("token"));

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(emailValidator)
        .isValid(TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail(), null);
    verify(userService)
        .getUserByLoginOrEmail(
            TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail(), null);
    verify(jwtTokenProviderImpl)
        .createToken(eq(TestConstants.AuthConstants.AUTH_REQUEST_DTO.getLoginOrEmail()), any());
  }

  @Test
  void login_InvalidCredentials_ThrowsBadCredentialsException() {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Invalid username or password"));

    assertThrows(
        BadCredentialsException.class,
        () -> authenticationService.login(TestConstants.AuthConstants.AUTH_REQUEST_DTO));

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
  }
}
