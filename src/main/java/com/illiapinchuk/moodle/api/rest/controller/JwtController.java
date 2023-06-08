package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for token requests. */
@RestController
@RequestMapping(value = "/token")
@RequiredArgsConstructor
@Slf4j
public class JwtController {

  private final JwtService jwtService;

  /**
   * This method is used to expire a JWT token.
   *
   * @param request The HTTP request containing the JWT token in the header
   * @return A response indicating that the token has been expired
   */
  @PostMapping("/expire")
  public ResponseEntity<Void> expireToken(@NotNull final HttpServletRequest request) {
    jwtService.expireJwtToken(request);
    log.info("Token was successfully expired");
    return ResponseEntity.ok().build();
  }
}
