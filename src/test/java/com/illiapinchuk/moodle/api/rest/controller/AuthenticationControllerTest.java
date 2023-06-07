package com.illiapinchuk.moodle.api.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.illiapinchuk.moodle.model.dto.AuthRequestDto;
import com.illiapinchuk.moodle.service.AuthenticationService;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
  private static final String VALID_LOGIN_OR_EMAIL = "testuser";
  private static final String VALID_PASSWORD = "password";
  private static final String EXAMPLE_TOKEN = "example-token";

  @Mock private AuthenticationService authenticationService;

  @InjectMocks private AuthenticationController authenticationController;

  @Test
  void testLogin_WhenValidRequest_ShouldReturnToken() {
    final var requestDto =
        AuthRequestDto.builder()
            .loginOrEmail(VALID_LOGIN_OR_EMAIL)
            .password(VALID_PASSWORD)
            .build();

    final var response = new HashMap<>();
    response.put("loginOrEmail", requestDto.getLoginOrEmail());
    response.put("token", EXAMPLE_TOKEN);

    when(authenticationService.login(requestDto)).thenReturn(response);

    final var result = authenticationController.login(requestDto);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(response, result.getBody());

    verify(authenticationService, times(1)).login(requestDto);
  }
}
