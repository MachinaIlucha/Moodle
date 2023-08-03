package com.illiapinchuk.moodle;

import com.illiapinchuk.moodle.config.RedisTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.EXISTING_USER_EMAIL_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.EXISTING_USER_LOGIN_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.NON_EXISTING_USER_EMAIL_AND_PASSWORD_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.NON_EXISTING_USER_LOGIN_AND_PASSWORD_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Path.LOGIN_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.USER_CONTROLLER_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {MoodleApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RedisTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AuthTests {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void whenExistingUsernameWithAnValidPasswordReturnOk() {
    final var resp =
        restTemplate.postForEntity(LOGIN_PATH, EXISTING_USER_LOGIN_AUTH_DTO, Map.class);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenExistingEmailWithAnValidPasswordReturnOk() {
    final var resp =
        restTemplate.postForEntity(LOGIN_PATH, EXISTING_USER_EMAIL_AUTH_DTO, Map.class);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenNonExistingUsernameWithAnIncorrectPasswordProvidedReturnUnauthorizedResponse() {
    final var resp =
        restTemplate.postForEntity(
            LOGIN_PATH, NON_EXISTING_USER_LOGIN_AND_PASSWORD_AUTH_DTO, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
  }

  @Test
  void whenNonExistingEmailWithAnIncorrectPasswordProvidedReturnUnauthorizedResponse() {
    final var resp =
        restTemplate.postForEntity(
            LOGIN_PATH, NON_EXISTING_USER_EMAIL_AND_PASSWORD_AUTH_DTO, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
  }

  @Test
  void whenExistingUsernameWithCorrectPasswordInvalidTokenThenGetUnauthorized() {
    final var resp =
        restTemplate.postForEntity(LOGIN_PATH, EXISTING_USER_LOGIN_AUTH_DTO, Map.class);

    final var token = (String) resp.getBody().get("token");

    final var headers = new HttpHeaders();
    headers.add("token", token + "someData");
    final var resp2 =
        restTemplate.exchange(
            USER_CONTROLLER_PATH, HttpMethod.GET, new HttpEntity<>(headers), String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, resp2.getStatusCode());
  }
}
