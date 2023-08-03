package com.illiapinchuk.moodle;

import com.illiapinchuk.moodle.config.RedisTestConfiguration;
import org.junit.jupiter.api.*;
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

import static com.illiapinchuk.moodle.common.TestConstants.Course.VALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.EXISTING_ADMIN_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Path.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {MoodleApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RedisTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtControllerTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  @Order(1)
  void whenExpireValidTokenShouldReturnStatusOk() {
    final var loginResp =
        restTemplate.postForEntity(LOGIN_PATH, EXISTING_ADMIN_AUTH_DTO, Map.class);

    final var token = (String) loginResp.getBody().get("token");
    final var headers = new HttpHeaders();
    headers.add("token", token);

    final var response =
        restTemplate.exchange(
            EXPIRE_JWT_TOKEN_PATH, HttpMethod.POST, new HttpEntity<>(headers), Void.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void whenExpireInvalidTokenShouldReturnStatusUnauthorized() {
    final var loginResponse =
        restTemplate.postForEntity(LOGIN_PATH, EXISTING_ADMIN_AUTH_DTO, Map.class);

    final var token = (String) loginResponse.getBody().get("token");

    final var headers = new HttpHeaders();
    headers.add("token", token + "invalidData");
    final var expireTokenResponse =
        restTemplate.exchange(
            EXPIRE_JWT_TOKEN_PATH, HttpMethod.POST, new HttpEntity<>(headers), Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, expireTokenResponse.getStatusCode());
  }

  @Test
  void whenExpireValidTokenShouldUserNotBeAbleToUserIt() {
    final var loginResp =
        restTemplate.postForEntity(LOGIN_PATH, EXISTING_ADMIN_AUTH_DTO, Map.class);

    final var token = (String) loginResp.getBody().get("token");
    final var headers = new HttpHeaders();
    headers.add("token", token);

    // Expire token
    restTemplate.exchange(
        EXPIRE_JWT_TOKEN_PATH, HttpMethod.POST, new HttpEntity<>(headers), Void.class);

    final var response =
        restTemplate.exchange(
            COURSE_WITH_ID_CONTROLLER_PATH,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class,
            VALID_COURSE_ID);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }
}
