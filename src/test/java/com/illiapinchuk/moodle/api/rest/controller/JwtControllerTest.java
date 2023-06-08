package com.illiapinchuk.moodle.api.rest.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.illiapinchuk.moodle.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
class JwtControllerTest {

  @Mock private JwtService jwtService;

  @InjectMocks private JwtController jwtController;

  @Test
  void expireTokenTest() {
    final var request = new MockHttpServletRequest();

    final var responseEntity = jwtController.expireToken(request);

    verify(jwtService, times(1)).expireJwtToken(request);
    assert (responseEntity.getStatusCode().is2xxSuccessful());
  }
}
