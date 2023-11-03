package com.illiapinchuk.moodle.configuration.security.basic;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.illiapinchuk.moodle.model.entity.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Custom basic authentication entry point. When user unauthorized throws error message in json
 * format and authentication challenge.
 */
@Slf4j
@RequiredArgsConstructor
public class CustomBasicAuthEntryPoint implements AuthenticationEntryPoint {

  private final JsonMapper jsonMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {

    // Set the WWW-Authenticate header, which prompts the browser to show the login dialog
    response.addHeader("WWW-Authenticate", "Basic realm=\"Moodle\", charset=\"UTF-8\"");

    log.error("Exception was thrown in entry point: ", authException);
    final var writer = response.getWriter();

    final var message = new ApiError(UNAUTHORIZED);
    message.setMessage("Not Valid Login info");

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    writer.println(jsonMapper.writeValueAsString(message));
  }
}
